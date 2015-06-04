package nl.uva.creed.game.strategyvisualizer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.repeated.History;
import nl.uva.creed.repeated.RepeatedGameStrategy;
import nl.uva.creed.repeated.impl.explicitautomaton.ExplicitAutomatonStrategyFactory;

public class StrategyVisualizer {

	public static String visualize(RepeatedGameStrategy strategy, int depth){
		StringBuffer buffer = new StringBuffer();
		List<History> histories =  HistoryGenerator.getAllHistories(depth);
		int i = 0;
		ArrayList<Integer> rightMost = weirdArray(histories.size());
		for (Iterator<History> iterator = histories.iterator(); iterator.hasNext();) {
			History history = (History) iterator.next();
			buffer.append(strategy.nextAction(history).toString());
			if (rightMost.contains(new Integer(i))) {
				buffer.append("\n");
			}
			i++;
		}
		return buffer.toString();
	}
	
	static private ArrayList<Integer> weirdArray(int length){
		ArrayList<Integer> ans = new ArrayList<Integer>();
		for (int i = 0; i < length; i++) {
			int j=1;
			double sum = 0;
			while (j<=i) {
				double temp = new Integer(j).doubleValue();
				sum = sum + Math.pow(2.0, temp );
				j++;
			}
			ans.add(new Double(sum).intValue());
		}
		return ans;
	}
	
	
	  // Returns a generated image.
    public static RenderedImage myCreateImage(String string) {
        int width = 224;
        int height = 300;
    
        // Create a buffered image in which to draw
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    
        // Create a graphics contents on the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();
    
        // Draw graphics
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
        
        StringTokenizer st = new StringTokenizer(string);
        int rows = st.countTokens();
        int fixedHeight= height/rows;
        int x= 0;
    	int y = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
        	//AQUI IMPRIME
            System.out.println(token);
            //------------------------------------
            int length = token.length();
            int widthCase = width/length;
            x = 0;
        	for (int i = 0; i < length; i++) {
            	
            	if (token.charAt(i) == 'C') {
            		g2d.setColor(Color.DARK_GRAY);
				}else{
					g2d.setColor(Color.LIGHT_GRAY);
				}
            	g2d.fillRect(x, y, widthCase, fixedHeight);
            	x = x + widthCase;
			}
            y = y+ fixedHeight;
            
        }
    
        
        
        
    
        // Graphics context no longer needed so dispose it
        g2d.dispose();
    
        return bufferedImage;
    }
	
	public static void main(String[] args) throws InvalidStrategyException {
		VisualizerGUI gui = new VisualizerGUI();
	    gui.go();
		
		@SuppressWarnings("unused")
		RenderedImage rendImage = myCreateImage(StrategyVisualizer.visualize(ExplicitAutomatonStrategyFactory.titForTat(), 5));
	    
	    // Write generated image to a file
	   /* try {
	        // Save as PNG
	        File file = new File("titForTat_bw.png");
	        ImageIO.write(rendImage, "png", file);
	    } catch (IOException e) {
	    }*/
	    
	  
	}
	
}
