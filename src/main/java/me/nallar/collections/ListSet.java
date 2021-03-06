package me.nallar.collections;

import me.nallar.modpatcher.PatcherLog;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

import java.util.Iterator;

public class ListSet extends SynchronizedList {
	@Override
	public synchronized boolean add(final Object o) {
		if (o instanceof World) {
			World world = (World) o;
			WorldProvider provider = world.provider;
			if (provider == null) {
				PatcherLog.error("Tried to add world " + world + " with null provider to bukkit dimensions.", new Throwable());
				return false;
			}
			Iterator<World> iterator = this.iterator();
			while (iterator.hasNext()) {
				World world_ = iterator.next();
				if (world_ == world) {
					return false;
				} else if (world_.provider == null) {
					PatcherLog.error("World " + world_ + " with null provider still in bukkit dimensions.", new Throwable());
					iterator.remove();
				} else if (provider.dimensionId == world_.provider.dimensionId) {
					//PatcherLog.error("Attempted to add " + Log.dumpWorld(world) + " to bukkit dimensions when " + Log.dumpWorld(world_) + " is already in it.", new Throwable());
					return false;
				}
			}
			return super.add(o);
		}
		return !this.contains(o) && super.add(o);
	}
}
