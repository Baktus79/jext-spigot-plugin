package me.tajam.jext.disc;

import lombok.Getter;
import me.tajam.jext.Log;
import me.tajam.jext.config.ConfigDiscData;
import me.tajam.jext.config.ConfigDiscData.Path;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class DiscContainer {

	public static final HashMap<Material, Sound> SOUND_MAP;

	static {
		SOUND_MAP = new HashMap<>();
		SOUND_MAP.put(Material.MUSIC_DISC_11, Sound.MUSIC_DISC_11);
		SOUND_MAP.put(Material.MUSIC_DISC_13, Sound.MUSIC_DISC_13);
		SOUND_MAP.put(Material.MUSIC_DISC_BLOCKS, Sound.MUSIC_DISC_BLOCKS);
		SOUND_MAP.put(Material.MUSIC_DISC_CAT, Sound.MUSIC_DISC_CAT);
		SOUND_MAP.put(Material.MUSIC_DISC_CHIRP, Sound.MUSIC_DISC_CHIRP);
		SOUND_MAP.put(Material.MUSIC_DISC_FAR, Sound.MUSIC_DISC_FAR);
		SOUND_MAP.put(Material.MUSIC_DISC_MALL, Sound.MUSIC_DISC_MALL);
		SOUND_MAP.put(Material.MUSIC_DISC_MELLOHI, Sound.MUSIC_DISC_MELLOHI);
		SOUND_MAP.put(Material.MUSIC_DISC_STAL, Sound.MUSIC_DISC_STAL);
		SOUND_MAP.put(Material.MUSIC_DISC_STRAD, Sound.MUSIC_DISC_STRAD);
		SOUND_MAP.put(Material.MUSIC_DISC_WAIT, Sound.MUSIC_DISC_WAIT);
		SOUND_MAP.put(Material.MUSIC_DISC_WARD, Sound.MUSIC_DISC_WARD);
		SOUND_MAP.put(Material.MUSIC_DISC_PIGSTEP, Sound.MUSIC_DISC_PIGSTEP);
		SOUND_MAP.put(Material.MUSIC_DISC_5, Sound.MUSIC_DISC_5);
		SOUND_MAP.put(Material.MUSIC_DISC_OTHERSIDE, Sound.MUSIC_DISC_OTHERSIDE);
		SOUND_MAP.put(Material.MUSIC_DISC_RELIC, Sound.MUSIC_DISC_RELIC);
	}

	private final HashMap<Integer, Material> DISC_MATERIAL = new HashMap<>();

	private final String title;
	private final String namespaceID;
	private final int customModelData;
	private boolean creeperDrop;
	private final ArrayList<String> lores;

	@Getter
	private final Material material;
	
	@Getter
	private final String author;

	public DiscContainer(ConfigDiscData configDiscData) {
		DISC_MATERIAL.put(0, Material.MUSIC_DISC_11);
		DISC_MATERIAL.put(1, Material.MUSIC_DISC_13);
		DISC_MATERIAL.put(2, Material.MUSIC_DISC_BLOCKS);
		DISC_MATERIAL.put(3, Material.MUSIC_DISC_CAT);
		DISC_MATERIAL.put(4, Material.MUSIC_DISC_CHIRP);
		DISC_MATERIAL.put(5, Material.MUSIC_DISC_FAR);
		DISC_MATERIAL.put(6, Material.MUSIC_DISC_MALL);
		DISC_MATERIAL.put(7, Material.MUSIC_DISC_MELLOHI);
		DISC_MATERIAL.put(8, Material.MUSIC_DISC_STAL);
		DISC_MATERIAL.put(9, Material.MUSIC_DISC_STRAD);
		DISC_MATERIAL.put(10, Material.MUSIC_DISC_WAIT);
		DISC_MATERIAL.put(11, Material.MUSIC_DISC_WARD);
		DISC_MATERIAL.put(12, Material.MUSIC_DISC_PIGSTEP);
		DISC_MATERIAL.put(13, Material.MUSIC_DISC_5);
		DISC_MATERIAL.put(14, Material.MUSIC_DISC_OTHERSIDE);
		DISC_MATERIAL.put(15, Material.MUSIC_DISC_RELIC);

		this.title = configDiscData.getName();
		this.author = configDiscData.getStringData(Path.AUTHOR);
		this.namespaceID = configDiscData.getStringData(Path.NAMESPACE);
		this.customModelData = configDiscData.getIntegerData(Path.MODEL_DATA);
		this.creeperDrop = configDiscData.getBooleanData(Path.CREEPER_DROP);
		this.lores = configDiscData.getLores();
		this.material = DISC_MATERIAL.get(customModelData);
	}

	public DiscContainer(ItemStack disc) throws IllegalStateException {
		if (isCustomDisc(disc)) {
			this.material = disc.getType();
			final ItemMeta meta = disc.getItemMeta();
			this.customModelData = meta.getCustomModelData();
			this.lores = new ArrayList<>(meta.getLore());
			final DiscPersistentDataContainer helper = new DiscPersistentDataContainer(meta);
			this.author = helper.getAuthor();
			this.namespaceID = helper.getNamespaceID();
			this.title = helper.getTitle();
		} else {
			throw new IllegalStateException("Custom disc identifier missing!");
		}
	}

	public ItemStack getDiscItem() {
		final ItemStack disc = new ItemStack(material);
		final ItemMeta meta = disc.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
		meta.setCustomModelData(customModelData);

		// Store custom disc data
		final DiscPersistentDataContainer helper = new DiscPersistentDataContainer(meta);
		helper.setAuthor(this.author);
		helper.setNamespaceID(this.namespaceID);
		helper.setTitle(this.title);
		helper.setIdentifier();

		// Lores and disc info
		final ArrayList<String> lores = new ArrayList<>();
		lores.add(new Log().gr(this.author).gr(" - ").gr(this.title).text());
		lores.addAll(this.lores);
		meta.setLore(lores);

		disc.setItemMeta(meta);
		return disc;
	}

	public String getNamespace() {
		return namespaceID;
	}

	public boolean canCreeperDrop() {
		return creeperDrop;
	}

	@Override
	public String toString() {
		return title;
	}

	private boolean isCustomDisc(final ItemStack disc) {
		if (disc == null || !disc.hasItemMeta()) {
			return false;
		}
		final ItemMeta meta = disc.getItemMeta();
		final DiscPersistentDataContainer helper = new DiscPersistentDataContainer(meta);
		try {
			if (!helper.checkIdentifier()) {
				return false;
			}
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

}