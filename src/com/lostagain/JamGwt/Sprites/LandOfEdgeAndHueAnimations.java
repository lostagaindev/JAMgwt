package com.lostagain.JamGwt.Sprites;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class LandOfEdgeAndHueAnimations {
	
	static LandOfEdgeAndHueSprites InternalSprites = (LandOfEdgeAndHueSprites) GWT
			.create(LandOfEdgeAndHueSprites.class);
			
				final static AbstractImagePrototype[] goalAnimation = {
					
					AbstractImagePrototype.create(InternalSprites.goal_animation0()),
					AbstractImagePrototype.create(InternalSprites.goal_animation1()),
					AbstractImagePrototype.create(InternalSprites.goal_animation2()),
					AbstractImagePrototype.create(InternalSprites.goal_animation3()),
					AbstractImagePrototype.create(InternalSprites.goal_animation4()),		
					AbstractImagePrototype.create(InternalSprites.goal_animation5()),
					AbstractImagePrototype.create(InternalSprites.goal_animation6()),
					AbstractImagePrototype.create(InternalSprites.goal_animation7()),
					AbstractImagePrototype.create(InternalSprites.goal_animation8()),
					AbstractImagePrototype.create(InternalSprites.goal_animation9()),	
					AbstractImagePrototype.create(InternalSprites.goal_animation10()),
					AbstractImagePrototype.create(InternalSprites.goal_animation11()),
					AbstractImagePrototype.create(InternalSprites.goal_animation12()),
					AbstractImagePrototype.create(InternalSprites.goal_animation13()),
					AbstractImagePrototype.create(InternalSprites.goal_animation14())	
				};
				final static AbstractImagePrototype[] dark_goalAnimation = {
						
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation0()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation1()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation2()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation3()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation4()),		
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation5()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation6()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation7()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation8()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation9()),	
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation10()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation11()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation12()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation13()),
						AbstractImagePrototype.create(InternalSprites.dark_goal_animation14())	
					};
					


				final static AbstractImagePrototype[] drawAttention = {
						AbstractImagePrototype.create(InternalSprites.draw_attention0()),
						AbstractImagePrototype.create(InternalSprites.draw_attention1()),
						AbstractImagePrototype.create(InternalSprites.draw_attention2()),
						AbstractImagePrototype.create(InternalSprites.draw_attention3()),
						AbstractImagePrototype.create(InternalSprites.draw_attention4()),
						AbstractImagePrototype.create(InternalSprites.draw_attention5()),
						AbstractImagePrototype.create(InternalSprites.draw_attention6()),
						
				};

				/**
				 * the above specified animations need to be given a associated name so the game script can access them (ie, in a sprite object)
				 */
				final static HashMap<String, AbstractImagePrototype[]> allGameSpecificAnimations = new HashMap<String, AbstractImagePrototype[]>(){
					{
						//scene animations;
						put("goal_animation",goalAnimation);
						put("dark_goal_animation",dark_goalAnimation);						
						put("draw_attention",drawAttention);
						//-----------------
					}
				};
}



