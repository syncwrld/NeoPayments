package me.toddydev.brpayments.core.model.product.icon;

import lombok.*;
import me.toddydev.brpayments.core.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Icon {

    private String name;
    private List<String> description;

    private Material material;
    private int id;

    public ItemStack stack() {
        description.replaceAll(s -> s.replace("&", "§"));
        return new ItemBuilder(material, id).name(name.replace("&", "§")).lore(description).amount(1).build();
    }

}
