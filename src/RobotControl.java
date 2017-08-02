import javax.swing.*;

class RobotControl
{
   private Robot r;
   public RobotControl(Robot r)
   {
       this.r = r;
   }

   public void control(int barHeights[], int blockHeights[])
   {

	 sampleControlMechanism(barHeights,blockHeights);
   }
   
   
   public void sampleControlMechanism(int barHeights[], int blockHeights[])
   {     
	     r.speedUp(2);
	     int h = 2;         // Initial height of arm 1
	     int w = 1;         // Initial width of arm 2  
	     int d = 0;         // Initial depth of arm 3
	     
	     int sourceHt = 0;
	     for(int i = 0; i < blockHeights.length; i++)
	     {
	         sourceHt += blockHeights[i];
	         // The source height is the sum of all the blocks in the blockHeights array, done due to 
	     }
	     
	     int currentBar  = 0;

	     // currentBar is used only for blocks of height 3, it remembers which bar the block should be dropped on after all loops
	     
	     int currentBlock  = blockHeights.length -1;
	     // currentBlock is the value for all blocks in the array, including 1, 2 and 3
	     // decreasing this value will change the position of the blockHeights[] array
	     
	     int targetCol1Ht = 0;    // The current height of the 1st and 2nd column
	     int targetCol2Ht = 0;    
	     
	     int stationaryContract = 7;
	     // this is the value for stationary contraction, what the original ContractAmt is 
	     // this is what the ContractAmt is set to at the start of a loop 
	     
	     int extendAmt = 10;

	     int blockHt = blockHeights[currentBlock];
	     // blockHt is the height of the current block being moved, determined by the currentBlock value
 
	     int clearance = clearanceCalculator( blockHt, sourceHt, blockHeights, barHeights);
	     // the clearanceCalculator is used to determine the value of the clearance for the block being moved
	     // it is updated at the very start and after a block has been dropped
	     
	     while ( currentBlock + 1 > 0)
	     // this is the start of the complete loop
	     { 
		     blockHt = blockHeights[currentBlock]; 
	    	 if (blockHt == 3)
	    	 {
	    		 
	    		 while ( h < clearance + 1 ) 
			     {
			         r.up();
			         // raise the height of arm 1
			         h++;
			         
			     }

			     System.out.println("Debug 1: height(arm1)= "+ h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 

			     // Bring arm 2 to column 10
			     while ( w < extendAmt )
			     {
			        
			        r.extend();
			        // raise the width of arm 2
			        w++;
			     }

			     System.out.println("Debug 2: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 

			     // lowering third arm - the amount to lower is based on current height
			     //  and the top of source blocks

			     // the position of the picker (bottom of third arm) is determined by h and d
			     while ( h - d > sourceHt + 1)   
			     {
			        
			        r.lower();
			        // arm 3 is being lowered
			        d++;
			     }

			     // picking the topmost block 
			     r.pick();

			     // When the top block of the source blocks is picked up, the source height decreases   
			     sourceHt -= blockHt;

			     // raising third arm all the way until d becomes 0
			     while ( d > 0)
			     {
			         r.raise();
			         d--;
			     } 

			     System.out.println("Debug 3: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 
			     
			     int contractAmt = stationaryContract;

			     // contract amount changes depending on the height of the block
			     // for blocks of height 3 it depends on how many other blocks have been dropped
			     // the first block is dropped at column 3 and decreases every time a block of height 3 is moved

			     while ( contractAmt > 0 )
			     {
			         r.contract();
			         contractAmt--;
			         w--;
			         // the contractAmt decreases here during this while loop all the way down to 0
			         // it is reset during every loop
			     }
			     stationaryContract--;
			     // Decreases by 1 so that the block picker does not try to drop a block on top of an already occupied bar

			     System.out.println("Debug 4: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 
			     
			     // lowering third arm
			     while ( (h - 1) - d - blockHt > barHeights[currentBar] )   
			     {
			         r.lower();
			         d++;
			     }

			     //increases the bar number value by 1 so that the block drops onto next bar
			     
			     System.out.println("Debug 5: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 
			     
			     // dropping the block      
			     r.drop();

			     // The height of currentBar increases by block just placed    
			     barHeights[currentBar] += blockHt;
			     
			     ++currentBar;
			     // increases position of where 3 height blocks go 
			     --currentBlock;
			     // increases position of where 2 height blocks go

			     if (currentBlock >= 0 && currentBlock < blockHeights.length)
			     // this if loop only activates if there is a value
			     {
			    	 blockHt = blockHeights[currentBlock]; 
				     // rechecks what the next block is so that the clearance can be calculated for the next block
			    	 clearance = clearanceCalculator( blockHt, sourceHt, blockHeights, barHeights);
			     }
			     
			     while ( h - 1 > clearance)
			     {
			    	 r.raise();
			    	 d--;
			    	 // raise Arm 3 before arm 1 is lowered, so that arm 3 doesn't collide with a bar
			    	 r.down();
			    	 h--;
			    	 // Lower Arm 1 so that it is just above the clearance
			     }
			     
			     while ( d > 0 )
			     {
			         r.raise();
			         d--;
			         // raising the third arm all the way
			     }
			     System.out.println("Debug 6: height(arm1)= " + h + " width (arm2) = " +
			                        w + " depth (arm3) =" + d); 
		
	    	 }
	    	 else if (blockHeights[currentBlock] == 2)
	    	 {
	    		 int contractAmt = 8;
	    		 // The contract amount does not need to be changed for block heights 2 and 1,
		    	 while ( h < clearance + 1 ) 
			     {
			         r.up();
			         //Most of basic movement is the same as with blocks of Height 1 and 3
			         h++;
			     }

			     System.out.println("Debug 1: height(arm1)= "+ h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 

			     while ( w < extendAmt )
			     {
			        // moving 1 step horizontally
			        r.extend();
			        w++;
			     }

			     System.out.println("Debug 2: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 

			     while ( h - d > sourceHt + 1)   
			     {
			        // lowering third arm
			        r.lower();
			        d++;
			     }

			     r.pick();

			     blockHt = 2;
 
			     sourceHt -= blockHt;

			     while ( d > 0)
			     {
			         r.raise();
			         d--;
			     } 

			     System.out.println("Debug 3: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 
	
			     // where the first bar is placed (from column 10)

			     while ( contractAmt > 0 )
			     {
			         r.contract();
			         contractAmt--;
			         w--;
			     }

			     System.out.println("Debug 4: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 
			     
			     // lowering third arm
			     while ( (h - 1) - d - blockHt > targetCol2Ht )   
			     {
			         r.lower();
			         d++;
			     }

			     //increases the bar number value by 1 so that the block drops onto next bar
			     
			     System.out.println("Debug 5: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 
			           
			     r.drop();

			     // The height of currentBar increases by block just placed    
			     targetCol2Ht += blockHt;
			     
			     --currentBlock;
			     // increases position of where 2 height and 1 height blocks go
			     // as there is no change to the bar heights 3-9, the barNumber doesn't change
			     
			     if (currentBlock >= 0 && currentBlock < blockHeights.length)
			     {
			    	 blockHt = blockHeights[currentBlock]; 
				     // rechecks what the next block is so that the clearance can be calculated for the next block
			    	 clearance = clearanceCalculator( blockHt, sourceHt, blockHeights, barHeights);
			     }
			     while ( h - 1 > clearance)
			     {
			    	 r.raise();
			    	 --d;
			    	 r.down();
			    	 h--;
			     }
			     // raising the third arm all the way
			     while ( d > 0 )
			     {
			    	 r.raise();
			         d--;
			     }
			     System.out.println("Debug 6: height(arm1)= " + h + " width (arm2) = " +
			                        w + " depth (arm3) =" + d); 
			     
	    	 }
	    	 else if (blockHeights[currentBlock] == 1)
	    	 {
	    		 // The coding is the exact same as for when the block height is = 2
	    		 // the only difference is how far it contracts, and which value increases
	    		 
	    		 int contractAmt = 9;
		    	 while ( h < clearance + 1 ) 
			     {
			         r.up();     
			         h++;
			     }

			     System.out.println("Debug 1: height(arm1)= "+ h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 

			     // Bring arm 2 to column 10
			     while ( w < extendAmt )
			     {
			        r.extend();
			        // Current width of arm2 being increased by 1
			        w++;
			     }

			     System.out.println("Debug 2: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 


			     // the position of the picker (bottom of third arm) is determined by h and d
			     while ( h - d > sourceHt + 1)   
			     {
			        // lowering third arm
			        r.lower();

			        // current depth of arm 3 being incremented
			        d++;
			     }

			     // picking the topmost block 
			     r.pick();

			     blockHt = 1;
  
			     sourceHt -= blockHt;

			     // raising third arm all the way until d becomes 0
			     while ( d > 0)
			     {
			         r.raise();
			         d--;
			     } 

			     System.out.println("Debug 3: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 

			     while ( contractAmt > 0 )
			     {
			         r.contract();
			         contractAmt--;
			         w--;
			     }

			     System.out.println("Debug 4: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 
			     
			     // lowering third arm
			     while ( (h - 1) - d - blockHt > targetCol1Ht )   
			     {
			         r.lower();
			         d++;
			     }
			     
			     System.out.println("Debug 5: height(arm1)= " + h + " width (arm2) = "+
			                        w + " depth (arm3) =" + d); 
			     
			     // dropping the block      
			     r.drop();
    
			     targetCol1Ht += blockHt;
			     // The height of currentBar increases by block just placed
			     
			     --currentBlock;
			     // decreases bar number
			     
			     if (currentBlock >= 0 && currentBlock < blockHeights.length)
			     {
			    	 blockHt = blockHeights[currentBlock]; 
				     // rechecks what the next block is so that the clearance can be calculated for the next block
			    	 clearance = clearanceCalculator( blockHt, sourceHt, blockHeights, barHeights);
			     }
			     while ( h - 1 > clearance)
			     {
			    	 r.raise();
			    	 --d;
			    	 r.down();
			    	 h--;
			     }
			     // raising the third arm all the way
			     while ( d > 0 )
			     {
			    	 r.raise();
			         d--;
			     }
			     System.out.println("Debug 6: height(arm1)= " + h + " width (arm2) = " +
			                        w + " depth (arm3) =" + d); 
	    	  }
		 }
   	} 
   public static int clearanceCalculator(int blockHt, int sourceHt, int blockHeights[], int barHeights[])
   {
	   int cl=12;
	   // cl is the clearance value that is returned to the other method
	   int maxBar = barHeights[0];
	   // maxBar is the highest bar height, the default is the first bar height given
	   
	   for (int i = 0; i < barHeights.length; i++) // loops for the amount of bars between columns 3 and 9
	   {
		   if (barHeights[i] > maxBar) //if the barHeight found is higher than the maxBar height
	       {
			   maxBar = barHeights[i];
			   // changes the maxBar value to the new highest barHeight
	       }
	   }
	   
	   if (blockHt == 3){
		   
		   if (sourceHt > maxBar + 3)
		   {
			   cl = sourceHt;
			   // the clearance value is set to the source height when the source height is higher than the highest bar value
			   // this is only used if the source height is either 12 or 11
		   }
		   else if (maxBar > 7)  {
			   int differenceBars = 10 - maxBar;
			   
			   int modDifferenceBars = differenceBars % 3;
			   // modDifferenceBars is the difference between the maximum bar and the source height
			   // this is used if the maxBar is 8 or 9 because if another bar has a height of 7, the block will not clear
			   
			   if (modDifferenceBars == 0)
			   {
				   cl = maxBar;
			   }
			   else if (modDifferenceBars == 1 || modDifferenceBars == 2)
			   {
				   cl = maxBar + modDifferenceBars;
			   }
			   
		   }
	   }
	   if (blockHt == 2 || blockHt == 1) // these two block will always have to clear the highest bar 
	   {
		   
		   if (sourceHt > maxBar + blockHt)
		   {
			   cl = sourceHt;
			   
		   }
		   else
		   {
			   cl = maxBar + blockHt; 
			   // regardless of the maxBar height, the block will always have to be able to clear
		   }  
	   }
	   return cl;
   }
} 




