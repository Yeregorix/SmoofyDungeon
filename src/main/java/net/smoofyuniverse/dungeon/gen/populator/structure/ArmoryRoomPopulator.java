/*
 * Copyright (c) 2017 Hugo Dupanloup (Yeregorix)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.smoofyuniverse.dungeon.gen.populator.structure;

import com.flowpowered.math.vector.Vector3d;
import net.smoofyuniverse.dungeon.gen.loot.ChestContentGenerator;
import net.smoofyuniverse.dungeon.gen.loot.EntityEquipmentGenerator;
import net.smoofyuniverse.dungeon.gen.populator.ChunkInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.BlockTrait;
import org.spongepowered.api.block.trait.EnumTraits;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.data.type.Hinges;
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.api.data.type.SlabTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class ArmoryRoomPopulator extends RoomPopulator {
	public static final ChestContentGenerator CHEST_GENERATOR = LibraryPopulator.CHEST_GENERATOR;
	public static final EntityEquipmentGenerator EQUIPMENT_GENERATOR;

	// This trait is not accessible via EnumTraits or BlockTypes.END_ROD.getTrait(name)
	public static final BlockTrait<?> END_ROD_FACING = BlockTypes.END_ROD.getTraits().iterator().next();

	public static final BlockState BLACK_CONCRETE = BlockTypes.CONCRETE.getDefaultState().with(Keys.DYE_COLOR, DyeColors.BLACK).get(),
			TOP_SLAB = BlockTypes.STONE_SLAB.getDefaultState()
					.withTrait(EnumTraits.STONE_SLAB_HALF, PortionTypes.TOP).get()
					.withTrait(EnumTraits.STONE_SLAB_VARIANT, SlabTypes.SMOOTH_BRICK).get(),
			IRON_DOOR_TOP = BlockTypes.IRON_DOOR.getDefaultState()
					.with(Keys.DIRECTION, Direction.SOUTH).get().with(Keys.PORTION_TYPE, PortionTypes.TOP).get(),
			IRON_DOOR_BOTTOM = BlockTypes.IRON_DOOR.getDefaultState()
					.with(Keys.DIRECTION, Direction.SOUTH).get().with(Keys.PORTION_TYPE, PortionTypes.BOTTOM).get(),
			END_ROD_UP = BlockTypes.END_ROD.getDefaultState().withTrait(END_ROD_FACING, "up").get(),
			END_ROD_DOWN = BlockTypes.END_ROD.getDefaultState().withTrait(END_ROD_FACING, "down").get();

	public ArmoryRoomPopulator() {
		super("armory_room");
		layers(2, 5);
		roomChance(0.001f, 0f);
	}

	@Override
	public boolean populateRoom(ChunkInfo info, World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		info.setFlag(layer, room, true);
		int floorY = y + getFloorOffset(c, x, y, z) + 1, ceilingY = y + getCeilingOffset(c, x, y, z) + 6;

		for (int cy = floorY; cy < ceilingY; cy++) {
			for (int dx = 1; dx < 7; dx++) {
				c.setBlock(x + dx, cy, z + 1, BLACK_CONCRETE);
				c.setBlock(x + dx, cy, z + 6, BLACK_CONCRETE);
				c.setBlockType(x + dx, cy, z + 7, BlockTypes.COBBLESTONE);
			}
			for (int dz = 1; dz < 7; dz++) {
				c.setBlockType(x, cy, z + dz, BlockTypes.COBBLESTONE);
				c.setBlock(x + 1, cy, z + dz, BLACK_CONCRETE);
				c.setBlock(x + 6, cy, z + dz, BLACK_CONCRETE);
				c.setBlockType(x + 7, cy, z + dz, BlockTypes.COBBLESTONE);
			}
		}

		for (int dx = 2; dx < 6; dx++) {
			for (int dz = 2; dz < 6; dz++)
				c.setBlock(x + dx, ceilingY - 1, z + dz, TOP_SLAB);
		}

		c.setBlock(x + 2, floorY, z + 2, END_ROD_UP);
		c.setBlock(x + 2, floorY, z + 5, END_ROD_UP);
		c.setBlock(x + 5, floorY, z + 2, END_ROD_UP);
		c.setBlock(x + 5, floorY, z + 5, END_ROD_UP);
		c.setBlock(x + 2, floorY + 1, z + 2, END_ROD_DOWN);
		c.setBlock(x + 2, floorY + 1, z + 5, END_ROD_DOWN);
		c.setBlock(x + 5, floorY + 1, z + 2, END_ROD_DOWN);
		c.setBlock(x + 5, floorY + 1, z + 5, END_ROD_DOWN);
		c.setBlockType(x + 2, floorY + 2, z + 2, BlockTypes.SEA_LANTERN);
		c.setBlockType(x + 2, floorY + 2, z + 5, BlockTypes.SEA_LANTERN);
		c.setBlockType(x + 5, floorY + 2, z + 2, BlockTypes.SEA_LANTERN);
		c.setBlockType(x + 5, floorY + 2, z + 5, BlockTypes.SEA_LANTERN);

		CHEST_GENERATOR.generateCarrier(c, x + 3, floorY, z + 5, r, BlockTypes.WHITE_SHULKER_BOX);
		CHEST_GENERATOR.generateCarrier(c, x + 4, floorY, z + 5, r, BlockTypes.WHITE_SHULKER_BOX);

		spawnArmorStand(c, r, x + 2, floorY, z + 3, -90);
		spawnArmorStand(c, r, x + 2, floorY, z + 4, -90);
		spawnArmorStand(c, r, x + 5, floorY, z + 3, 90);
		spawnArmorStand(c, r, x + 5, floorY, z + 4, 90);

		c.setBlock(x + 3, floorY, z + 1, IRON_DOOR_BOTTOM.with(Keys.HINGE_POSITION, Hinges.RIGHT).get());
		c.setBlock(x + 3, floorY + 1, z + 1, IRON_DOOR_TOP.with(Keys.HINGE_POSITION, Hinges.RIGHT).get());
		c.setBlock(x + 4, floorY, z + 1, IRON_DOOR_BOTTOM.with(Keys.HINGE_POSITION, Hinges.LEFT).get());
		c.setBlock(x + 4, floorY + 1, z + 1, IRON_DOOR_TOP.with(Keys.HINGE_POSITION, Hinges.LEFT).get());

		if (c.getBlockType(x + 3, floorY - 1, z - 1) == BlockTypes.AIR && c.getBlockType(x + 3, floorY - 2, z - 1) == BlockTypes.AIR)
			c.setBlockType(x + 3, floorY - 1, z - 1, BlockTypes.COBBLESTONE);
		if (c.getBlockType(x + 4, floorY - 1, z - 1) == BlockTypes.AIR && c.getBlockType(x + 4, floorY - 2, z - 1) == BlockTypes.AIR)
			c.setBlockType(x + 4, floorY - 1, z - 1, BlockTypes.COBBLESTONE);

		c.setBlockType(x + 3, floorY, z, BlockTypes.AIR);
		c.setBlockType(x + 4, floorY, z, BlockTypes.AIR);
		c.setBlockType(x + 3, floorY + 1, z, BlockTypes.AIR);
		c.setBlockType(x + 4, floorY + 1, z, BlockTypes.AIR);

		c.setBlockType(x + 3, floorY, z - 1, BlockTypes.AIR);
		c.setBlockType(x + 4, floorY, z - 1, BlockTypes.AIR);
		c.setBlockType(x + 3, floorY + 1, z - 1, BlockTypes.AIR);
		c.setBlockType(x + 4, floorY + 1, z - 1, BlockTypes.AIR);

		return true;
	}

	public static boolean spawnArmorStand(Extent c, Random r, int x, int y, int z, double yaw) {
		return spawnArmorStand(c, r, new Vector3d(x + 0.5d, y, z + 0.5d), new Vector3d(0, yaw, 0));
	}

	public static boolean spawnArmorStand(Extent c, Random r, Vector3d pos, Vector3d rot) {
		ArmorStand e = (ArmorStand) c.createEntity(EntityTypes.ARMOR_STAND, pos);
		e.setRotation(rot);
		EQUIPMENT_GENERATOR.fill(e, r);
		return c.spawnEntity(e);
	}

	static {
		EQUIPMENT_GENERATOR = EntityEquipmentGenerator.builder()
				.add(EquipmentTypes.BOOTS, ItemTypes.LEATHER_BOOTS, 0.3)
				.add(EquipmentTypes.BOOTS, ItemTypes.IRON_BOOTS, 0.5)
				.add(EquipmentTypes.BOOTS, ItemTypes.GOLDEN_BOOTS, 0.2)
				.add(EquipmentTypes.BOOTS, ItemTypes.DIAMOND_BOOTS, 0.05)
				.addNone(EquipmentTypes.BOOTS, 2)

				.add(EquipmentTypes.LEGGINGS, ItemTypes.LEATHER_LEGGINGS, 0.3)
				.add(EquipmentTypes.LEGGINGS, ItemTypes.IRON_LEGGINGS, 0.5)
				.add(EquipmentTypes.LEGGINGS, ItemTypes.GOLDEN_LEGGINGS, 0.2)
				.add(EquipmentTypes.LEGGINGS, ItemTypes.DIAMOND_LEGGINGS, 0.05)
				.addNone(EquipmentTypes.LEGGINGS, 2)

				.add(EquipmentTypes.CHESTPLATE, ItemTypes.LEATHER_CHESTPLATE, 0.3)
				.add(EquipmentTypes.CHESTPLATE, ItemTypes.IRON_CHESTPLATE, 0.5)
				.add(EquipmentTypes.CHESTPLATE, ItemTypes.GOLDEN_CHESTPLATE, 0.2)
				.add(EquipmentTypes.CHESTPLATE, ItemTypes.DIAMOND_CHESTPLATE, 0.05)
				.addNone(EquipmentTypes.CHESTPLATE, 2)

				.add(EquipmentTypes.HEADWEAR, ItemTypes.LEATHER_HELMET, 0.3)
				.add(EquipmentTypes.HEADWEAR, ItemTypes.IRON_HELMET, 0.5)
				.add(EquipmentTypes.HEADWEAR, ItemTypes.GOLDEN_HELMET, 0.2)
				.add(EquipmentTypes.HEADWEAR, ItemTypes.DIAMOND_HELMET, 0.05)
				.addNone(EquipmentTypes.HEADWEAR, 2)
				.build();
	}
}
