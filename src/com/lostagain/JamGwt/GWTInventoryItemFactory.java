package com.lostagain.JamGwt;

import java.util.logging.Logger;

import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.Interfaces.IsPopupPanel;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.Jam.InventoryItems.InventoryItemFactory;
import com.lostagain.Jam.InventoryItems.IsImageWithAlphaItem;
import com.lostagain.Jam.SceneObjects.InventoryObjectState;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;


//gwt specific popup types
import com.lostagain.JamGwt.InventoryObjectTypes.ConceptPopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.DummyPopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.ImagePopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryItem;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryPanel;
import com.lostagain.JamGwt.InventoryObjectTypes.embedPopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.flashPopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.imageWithAlphaItem;
import com.lostagain.JamGwt.InventoryObjectTypes.moviePopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.overlayPopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.textScroller;
import com.lostagain.JamGwt.InventoryObjectTypes.toggleImageGroupPopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.youtubePopUp;
//-------------------------------

public class GWTInventoryItemFactory extends InventoryItemFactory {

	static Logger Log = Logger.getLogger("GwtJAM.GWTInventoryItemFactory");
	
	@Override
	public IsImageWithAlphaItem createNewImageWithAlphaItem_impl(String ImagelocationSet, String NewImageDiscription) {
	
		imageWithAlphaItem imagepop = new imageWithAlphaItem(ImagelocationSet,NewImageDiscription); 
		
		return imagepop;
	}

	@Override
	public IsPopupPanel createImageZoomPopup_impl(IsInventoryItem zoomThis, String ImageURL, int sizex, int sizey, String imageDiscription, String popupTitle)
	{
		
		ImageZoomPopUp mag                       =  new ImageZoomPopUp(ImageURL,imageDiscription,sizex,sizey);
		TitledPopUpWithShadow ItemWithShadow     =  new TitledPopUpWithShadow(zoomThis,"30%","25%",popupTitle,mag);
		
		return ItemWithShadow;
	}

	
	@Override
	public IsInventoryItemPopupContent createInventoryItemsPopupContents_impl(inventoryItemTypes ItemsType, final String ItemsName,
			String ItemsDiscription, String ItemsURL, String ItemsTitle, String size_x, String size_y,
			boolean is_magnifiable, String Embed){

		
		//InventoryItem newInventoryIcon;
		IsInventoryItemPopupContent ItemPopUp;
		

		//ItemsType = ItemsType.toLowerCase();

		switch (ItemsType) {
		case picture:
		{
			int rsize_x = Integer.parseInt(size_x);
			int rsize_y = Integer.parseInt(size_y);

			ItemPopUp = new ImagePopUp("InventoryItems/" + ItemsName + "/" + ItemsName+".jpg", ItemsDiscription, rsize_x, rsize_y);

			break;
		}
		case movie:
		{
			ItemPopUp = new moviePopUp("InventoryItems/" + ItemsName + "/" + ItemsName+ ".mov", size_x, size_y);

			break;
		}
		case youtube:
		{
			ItemPopUp = new youtubePopUp(ItemsURL, 425,	345);

			break;
		}
		case embed:
		{
			ItemPopUp = new embedPopUp(ItemsURL, size_x,		size_y, Embed);

			JAMcore.GameLogger.info(" embed code="	+ Embed);
			
			break;
		}
		case pngpicture:
		{
			int rsize_x = Integer.parseInt(size_x);
			int rsize_y = Integer.parseInt(size_y);
			ItemPopUp = new ImagePopUp(			"InventoryItems/" + ItemsName + "/" + ItemsName			+ ".png", ItemsDiscription, rsize_x, rsize_y);
		//	newInventoryIcon = new InventoryItem(ItemPopUp,  title,				this_inventory,iconsstate,ItemDataString);

			break;
		}
		case flash:
		{
			flashPopUp FlashItem = new flashPopUp("InventoryItems/"	+ ItemsName + "/" + ItemsName + ".swf", size_x,	size_y);
			ItemPopUp = FlashItem;
			//newInventoryIcon = new InventoryItem(ItemPopUp,  title,					this_inventory,iconsstate,ItemDataString);

			if (is_magnifiable) {
				FlashItem.Magnifable = true;
				JAMcore.GameLogger.info("flashitem mag set to.."	+ FlashItem.Magnifable);
			}


			break;
		}
		case toggleitemgroup:
		{
			// increase the left-to-load list (as tigs have sub-items)
			JAMcore.itemsLeftToLoad++;
			Log.info("increaseing items left to load to:"		+ JAMcore.itemsLeftToLoad);
			toggleImageGroupPopUp TIGitem = new toggleImageGroupPopUp(
					ItemsName, ItemsDiscription, size_x, size_y,false);

			ItemPopUp = TIGitem;
			//newInventoryIcon = new InventoryItem(ItemPopUp,  title,	this_inventory,iconsstate,ItemDataString);

			if (is_magnifiable) {
				TIGitem.Magnifable = true;
				JAMcore.GameLogger.info("flashitem mag set to.."	+ TIGitem.Magnifable);
			}
			break;
		}
		case pngtoggleitemgroup:
		{
			// increase the left-to-load list(as tigs have sub-items)
			JAMcore.itemsLeftToLoad++;
			Log.info("increaseing items left to load to:"		+ JAMcore.itemsLeftToLoad);
			toggleImageGroupPopUp TIGitem = new toggleImageGroupPopUp(ItemsName, ItemsDiscription, size_x, size_y,true);

			ItemPopUp = TIGitem;
		//	newInventoryIcon = new InventoryItem(ItemPopUp,  title,this_inventory,iconsstate,ItemDataString);

			if (is_magnifiable) {
				TIGitem.Magnifable = true;
				JAMcore.GameLogger.info("flashitem mag set to.."+ TIGitem.Magnifable);
			}
			break;
		}
		case overlay:
		{
			ItemPopUp = new overlayPopUp();
			break;
		}
		case magnifyingglass:
		{
			ToolPopUp mag = new ToolPopUp(ItemsDiscription, ItemsTitle);
			mag.tooltype = "MAGNIFYINGGLASS";
			mag.POPUPONCLICK = false;
			ItemPopUp = mag;
			break;
		}
		case textscroll:
		{
			textScroller textScroll = new textScroller(		"InventoryItems/" + ItemsName + "/" + ItemsName		+ ".html");
			Log.info("\n ----make text scroller"							+ ItemsDiscription);
			ItemPopUp = textScroll;
			break;
		}
		case dummy:
		{
			ItemPopUp = new DummyPopUp(ItemsTitle);
			break;
		}
		case concept:
		{
			ItemPopUp = new ConceptPopUp(ItemsDiscription,	ItemsTitle);
			break;
		}
		default:
		{
			// default concept widget
			ItemPopUp = new ConceptPopUp(ItemsName,ItemsTitle);
			break;
		}

		}
		return ItemPopUp;

		
	}

	@Override
	public IsInventoryItem createInventoryItem_impl(IsInventoryItemPopupContent itemPopUp, String title,
			InventoryPanelCore this_inventory, InventoryObjectState iconsstate, String itemDataString) {
		
		return  new InventoryItem(itemPopUp,  title,(InventoryPanel) this_inventory,iconsstate,itemDataString);
	}

}
