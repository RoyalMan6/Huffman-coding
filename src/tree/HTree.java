package tree ;

import java.io.*;
import javax.swing.*;
import java.util.Stack;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

public class HTree  {
    private static HNode Treeit;//the tree itself
    private String encode="";//The encoded message
    private String original="";//The original message
    private PriorityQueue<HNode> q;//will store the nodes temporarily to construct the HTree
    private HashMap<Character,HNode> map;//will store the characters and associated HNodes 
    private Collection<HNode> nodes;//Will the store the HNodes containing characters 

    // for choosing the FILE
    public static void main(String[] args)throws FileNotFoundException{
        JFileChooser browser = new JFileChooser();
        int dialog=browser.showOpenDialog(null);
        if(dialog==browser.APPROVE_OPTION){ // if user chose the file okay and if not the program will end
            File f=browser.getSelectedFile(); //get the file 
            HTree tree=new HTree();
            tree.encode(f);//create a tree and encode based off of the contents of the file 
            Iterator i1=tree.nodes.iterator();//to display the character-frequency table
            
            
            System.out.println("The original string is:"+tree.original);//display the original contents of the file
            System.out.println("\n");
            System.out.println("-----------------------------------Frequency-------------------------------");
            while(i1.hasNext()){
                HNode n=(HNode)i1.next();
                switch(n.data){
              //if the character is a space character 
                case' ': System.out.println("Char:<space>"+"  "+"Frequency:"+n.frequency);//a way to represent a space character 
                break;
              //if the character is a new line character
                case'\n': System.out.println("Char:<nl>"+"  "+"Frequency:"+n.frequency);//a way to represent a new line character 
                break;
                     
                default: System.out.println("Char:"+n.data+"  "+"Frequency:"+n.frequency);//display each character and its code
                }
            }
            
 
            Iterator i2=tree.nodes.iterator();//to display the character-code table
            System.out.println("\n");
            System.out.println("-----------------------------------Code-----------------------------------");
            while(i2.hasNext()){
                
                HNode n=(HNode)i2.next();
                switch(n.data){
                case' ': System.out.println("Char:<sp>"+"  "+"Code:"+n.code);//display each character and its code
                break;
                case'\n': System.out.println("Char:<nl>"+"  "+"Code:"+n.code);//display each character and its code
                break;
                
                default:System.out.println("Char:"+n.data+"  "+"Code:"+n.code);//display each character and its code
                
                }
            }

            System.out.println("\nThe encoded string is:"+tree.encode);//display the encoded version of the original message
         
            System.out.println("\nThe decoded string is:"+tree.decode(tree.encode));//display the decoded version of the message(should be the same as the original message)
        
            System.out.println("\n");
            //to display the tree
            displayTree() ;
            
            
            System.out.print("\nAverage number of bits used per character (BEFORE)= 8");
            
            double  average2=tree.getAverageAFTER();//calculate the average number of bits used per symbol
            //System.out.println("Average # of bits used per character:"+average);//display it
            System.out.print("\nAverage number of bits used per character (AFTER)= " + average2);//display it
            
        }
    }

 
     // Will call other methods to making a map and constructing the tree. This method will encodes the message.
    public void encode(File f)throws FileNotFoundException{
        makeMap(f);//make a map that contains each character and its HNode
        makeTree();//make a tree that contains the characters as leaf nodes
        // after we make the tree we will pass it to getCharCode to write the code
        getCharCodes(Treeit, "");//generate the codes for each character, THE TREE
        Scanner s=new Scanner(f);//to read from the file 
        String line;//save each line in the file
        char[] chars;//each character in a given line
        while(s.hasNextLine()){
            line=s.nextLine();//store a line
            original+=line;//save this part of the message
            chars=line.toCharArray();//tocharArray will split the String and will put each char in the array,to be able to examine each character
            for(int i=0;i<chars.length;i++){
                encode+=map.get(chars[i]).code;//get the encoded version of the character and save it
            }
            if(s.hasNextLine()){
                original+="\n";
                encode+=map.get('\n').code;//adding back the newline character if necessary 
            }
        }
    }
    
    //This method will put right 1 and left 0 until we reach to the leaves
    private void getCharCodes(HNode n, String code){
        if(n.left==null&&n.right==null){//if the current node is a leaf
            n.code=code;//record its code (was originally n.code)
            return;
        }
        //Complete until the right and left null
        getCharCodes(n.right,code+"1");//go left mark the path with a 0
        getCharCodes(n.left,code+"0");//go right, mark the path with a 1
    }

    
     //This method will read from a file and create a map with the characters as keys and HNodes as values.If an HNode already exists, the frequency is incremented.
    public void makeMap(File f)throws FileNotFoundException{
        map=new HashMap<Character, HNode>();//initialize the map 
        Scanner s=new Scanner(f);//to read from the file
        String line="";//will store each line
        char[] chars;//will store each character of a given line
        HNode n=null;
        while(s.hasNextLine()){//while there are more lines 
            line=s.nextLine();//store a line
            chars=line.toCharArray();//store each character of the current line
            for(int i=0;i<chars.length;i++){//for each line 
                if(map.containsKey(chars[i])){//if its already in the map increment the frequency of the associated HNode
                    map.get(chars[i]).frequency+=1;

                }
                else{//if the current character isn't in the map, store it.
                    n=new HNode(chars[i],1,null,null); // 1 for the frequency in the first time
                    map.put(chars[i],n);
                }
            }
            if(s.hasNextLine()){//adding back the newline character when necessary
                if(map.containsKey('\n')){//if the newline character is already in the map, increment the frequency of the associated HNode 
                    map.get('\n').frequency+=1;

                }
                else{//otherwise store it
                    n=new HNode('\n',1,null,null);
                    map.put('\n',n);
                }
            }
        }
    }
    
    //This method will construct a Huffman tree that will contain HNodes with characters(only the leaves have characters).
    public void makeTree(){
        //The code below will add leaves containing characters and associated frequencies
        nodes=map.values(); //get a collection of the values
        Iterator it=nodes.iterator();
        q=new PriorityQueue<HNode>(); //initialize PQ
        
        while(it.hasNext()){ //here we will add all Nodes to the queue
            q.add((HNode)it.next()); //add the leaves 
        }
        HNode parent=null;
        HNode left=null;
        HNode right=null;
        while(q.size()>1){//while two nodes can still be taken out
            left=q.remove();//remove the lowest 
            right=q.remove();//remove the second lowest
            parent=new HNode(null,left.frequency+right.frequency,left,right);//create a new node containing the two removed nodes as its left and right
            q.add(parent);//add the newly created node to the 
        }//when the loop ends there should be one node left in the priority queue that contains the tree
        Treeit=q.remove();//store the tree
    }

    
     //This method will decode the encoded message
    public String decode(String code){
        HNode p=Treeit;//create a pointer
        String ORGMessage="";//here we will store the actual message 
        for(int i=0;i<code.length();i++){// length of the encoded message
            if(p.left==null&&p.right==null){//if a leaf is reached 
                ORGMessage+=p.data;//add the associated character to the decoded message
                p=Treeit;//reset the pointer
            }

            if(code.charAt(i)=='0'){//it the current part of the encoded message is a 0
                p=p.left;     //To the left
            }
            else if(code.charAt(i)=='1'){//if its a 1 
                p=p.right;   //To the right
            }
        }
        ORGMessage+=p.data; //the last char
        return ORGMessage;
    }

    
    public static void displayTree() {
		Stack Stack1 = new Stack(); //global stack
		Stack1.push(Treeit);
		int Blanks = 64; // make it 32 if u would
		boolean isRowEmpty = false;
		System.out.println("-----------------------------------Tree-----------------------------------");
		
		while(isRowEmpty==false) {
			Stack Stack2 = new Stack(); //local stack
			isRowEmpty = true;
			for(int j=0; j<Blanks; j++)
				System.out.print(' ');
			while(Stack1.isEmpty()==false)
			{
				HNode temp = (HNode)Stack1.pop();
				
				if(temp != null) {
					if(temp.left == null || temp.right == null)
					System.out.print(temp.data+""+temp.frequency);
					else
					System.out.print(""+temp.frequency);
					
					Stack2.push(temp.left);
                    Stack2.push(temp.right);
					if(temp.left != null || temp.right != null)
						isRowEmpty = false;
				}
				else
				{
					System.out.print("--"); //leaves null
					Stack2.push(null);
					Stack2.push(null);
				}
				for(int j=0; j<Blanks*2-2; j++)
					System.out.print(' ');
			} // end while globalStack not empty
			System.out.println();
			Blanks /= 2;
			while(Stack2.isEmpty()==false)
				Stack1.push( Stack2.pop() );
		} // end while isRowEmpty is false
		System.out.println("--------------------------------------------------------------------------");
	} // end displayTree()
    
  //calculate the average number of bits per symbol after
    public double getAverageAFTER(){
        Iterator i=nodes.iterator();
        double sum=0;//accumulator 
        while(i.hasNext()){
            HNode n=(HNode) i.next();//pointer
            sum+=n.code.length()*n.frequency;//add the number of bits for that character

        }
        double average=sum/original.length();
        return average;
    }

    class HNode implements Comparable{
        private Character data;// character 
        private Integer frequency=0;//Character Repetition
        private HNode left,right;// left and right subtree or leaves
        private String code;// the unique code
     
        // initializes , every node will have data "Character", frequency is the Character Repetition , and LEFT,RIGHT child
        public HNode(Character data,Integer frequency,HNode left,HNode right){
            this.data=data;
            this.frequency=frequency;
            this.left=left; 
            this.right=right;
        }

       
         // obj is the object we will compare, compares two Nodes -1 means it greater than the current, 1 if it less than than the current,0 if it equal
        public int compareTo(Object obj){
            if(!(obj instanceof HNode)){//if the object isn't an HNode
                throw new IllegalArgumentException("It wasn't a node.");
            }
            HNode node=(HNode)obj; //make it an HNode because the previous if statement failed
            if(node.frequency>frequency){//if the parameter's frequency is greater than the current node's frequency
                return -1;
            }
            else if(node.frequency<frequency){//if the parameter's frequency is less than the current node's frequency
                return 1;
            }
            return 0;//they're equal at this point so return 0;
        }
    }
}


