package slimeknights.mantle.fluid.transfer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.function.TriFunction;
import slimeknights.mantle.Mantle;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.mantle.util.JsonHelper;

import java.lang.reflect.Type;
import java.util.function.Consumer;

/** Fluid transfer info that empties a fluid from an item */
@RequiredArgsConstructor
public class EmptyFluidContainerTransfer implements IFluidContainerTransfer {
  public static final ResourceLocation ID = Mantle.getResource("empty_item");

  private final Ingredient input;
  private final ItemOutput filled;
  protected final FluidStack fluid;

  @Override
  public void addRepresentativeItems(Consumer<Item> consumer) {
    for (ItemStack stack : input.getItems()) {
      consumer.accept(stack.getItem());
    }
  }

  @Override
  public boolean matches(ItemStack stack, FluidStack fluid) {
    return input.test(stack);
  }

  /** Gets the contained fluid in the given stack */
  protected FluidStack getFluid(ItemStack stack) {
    return fluid;
  }

  @Override
  public TransferResult transfer(ItemStack stack, FluidStack fluid, Storage<FluidVariant> handler) {
    FluidStack contained = getFluid(stack);
    long simulated = handler.simulateInsert(contained.getType(), contained.getAmount(), null);
    if (simulated == this.fluid.getAmount()) {
      try (Transaction t = TransferUtil.getTransaction()) {
        long actual = handler.insert(contained.getType(), contained.getAmount(), t);
        if (actual > 0) {
          if (actual != this.fluid.getAmount()) {
            Mantle.logger.error("Wrong amount filled from {}, expected {}, filled {}", Registry.ITEM.getKey(stack.getItem()), this.fluid.getAmount(), actual);
          }
          return new TransferResult(filled.get().copy(), contained, false);
        }
        t.commit();
      }
    }
    return null;
  }

  @Override
  public JsonObject serialize(JsonSerializationContext context) {
    JsonObject json = new JsonObject();
    json.addProperty("type", ID.toString());
    json.add("input", input.toJson());
    json.add("filled", filled.serialize(false));
    json.add("fluid", RecipeHelper.serializeFluidStack(fluid));
    return json;
  }

  /** Unique loader instance */
  public static final JsonDeserializer<EmptyFluidContainerTransfer> DESERIALIZER = new Deserializer<>(EmptyFluidContainerTransfer::new);

  /**
   * Generic deserializer
   */
  public record Deserializer<T extends EmptyFluidContainerTransfer>(TriFunction<Ingredient,ItemOutput,FluidStack,T> factory) implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonObject json = element.getAsJsonObject();
      Ingredient input = Ingredient.fromJson(JsonHelper.getElement(json, "input"));
      ItemOutput filled = ItemOutput.Loadable.REQUIRED_ITEM.getIfPresent(json, "filled");
      FluidStack fluid = RecipeHelper.deserializeFluidStack(GsonHelper.getAsJsonObject(json, "fluid"));
      return factory.apply(input, filled, fluid);
    }
  }
}
