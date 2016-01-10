using System;
using System.IO;

using MiNET.Plugins;
using MiNET.Plugins.Attributes;

using MiNET;
using MiNET.Net;
using MiNET.Utils;

namespace PRCore
{
	[Plugin(Author = "TheDiamondYT", Description = "PocketRealms Core", PluginName = "PRCore", PluginVersion = "1.0")]
    public class PRCore : Plugin
    {
		private string _basepath = MiNET.Utils.Config.GetProperty("PluginDirectory", "Plugins") + "\\PRCore";
		
		protected override void onEnable()
		{
			if (!Directory.Exists(_basepath)) Directory.CreateDirectory(_basepath);
			//TODO
		}
		
		[Command(Command = "version")]
		public void Version(Player player)
		{
			player.SendMessage("This server is running MiNET (https://github.com/niclasolofsson/minet)", type: MessageType.Raw);
		}
		
		[Command(Command = "gm")]
		[Authorize(Users = "Mack", "TheDiamondYT7"]
		public void GameMode(Player player, int gameMode)
		{
			if (gameMode == 1)
			{
				player.Inventory.Slots.Clear();
				player.Inventory.Slots.AddRange(InventoryUtils.CreativeInventoryItems);
			}

			player.SendPackage(new McpeStartGame
			{
				seed = -1,
				generator = 1,
				gamemode = gameMode,
				entityId = player.EntityId,
				spawnX = (int) player.Level.SpawnPoint.X,
				spawnY = (int) player.Level.SpawnPoint.Y,
				spawnZ = (int) player.Level.SpawnPoint.Z,
				x = player.KnownPosition.X,
				y = player.KnownPosition.Y,
				z = player.KnownPosition.Z
			});

			{
				McpeContainerSetContent creativeContent = McpeContainerSetContent.CreateObject();
				creativeContent.windowId = (byte) 0x79;
				creativeContent.slotData = player.Inventory.GetSlots();
				creativeContent.hotbarData = player.Inventory.GetHotbar();
				player.SendPackage(creativeContent);
			}
		}

    }
}
