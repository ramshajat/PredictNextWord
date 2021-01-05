
package PredictText;
/**
 023-19-0060 ramsha
 */
import java.io.*;

public class Main {
    Node root;
	public static int fileNo;
	public static int WordsNo;
	public String[] words =new String[10000];
	public int[] count =new int[10000];
        
        //constructor thta recieve file and store its words in trie 
       public  Main(String filename){
           try {
				fileNo=0;
				BufferedReader reader=new BufferedReader(new FileReader(filename));
				String line=null;
				root=new Node();
				while((line=reader.readLine())!= null) {
					this.insert(line);
				}			
				reader.close();
				fileNo=1;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
       }
       // method to insert in trie
       public void insert(String word){
           char ch;
		Node temp=root;
		if(search(word)==0){
			for(int i=0;i<word.length();i++){
				ch=word.charAt(i);
				//System.out.println(ch[i]);
				if(temp.next[ch]==null){
					Node temp2=new Node();
					temp.next[ch]=temp2;
					temp.next[ch].position++;
					temp=temp2;
				}
				else{
					temp.next[ch].position++;
					temp=temp.next[ch];
				}
			}
			temp.end=1;
			if(fileNo==1){
				try {
				BufferedWriter f=new BufferedWriter(new FileWriter("Dictionary.txt",true));
				f.write(word+"\n");
				f.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		else
		{
			//System.out.println("exists");
			for(int i=0;i<word.length();i++){
				ch=word.charAt(i);
				temp=temp.next[ch];
				temp.position++;
				//System.out.println("Word is at level "+(i+1)+":"+temp.position+":"+(char)ch);
			}
                        if(fileNo==1){
				try {
						BufferedWriter f=new BufferedWriter(new FileWriter("Dictionary.txt",true));
						f.write(word+"\n");
						f.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
       }
       //search word in trie if exist return true else false
       public int search(String word){
           char ch;
		int bool=1;
		Node temp=root;
		for(int i=0;i<word.length()&&bool==1;i++){
			ch=word.charAt(i);
			if(temp.next[ch]!=null){
				temp=temp.next[ch];
			}
			else
				bool=0;
		}
		if(bool==1&&temp.end==1){
			return 1;
		}
		else{
			return 0;
		}
	}
       
       //predict word 
       public String[] predictor(String text){
           char ch;
		String word="";
		int wordCount;
		Node temp=root;
	
		for(int i=0;i<text.length();i++){
                    WordsNo=0;
                    ch=text.charAt(i);
			if(ch==' '||ch=='\n'||ch==','||ch=='.')
				break;
			if(temp.next[ch]!=null){
				temp=temp.next[ch];
				wordCount=temp.position;
				word=word+ch;
				WordsNo=0;
                                for(int k=0;k<12;k++)
                                    words[k]="";
				
				Traverse(word,wordCount,temp);
				order();
                              
                                if(i==text.length()-1)
                                    return words;
                                
			}
                            
			
		}
		return words;
       }
       
       //sort data according to next character and give most relevent word 
       public void order(){
		int pos,temp;
		String stemp;
		//System.out.println("No of Words:"+noOfWords);
		for(int j=0;j<WordsNo-1;j++){
			pos=j;
			for(int k=j+1;k<WordsNo;k++){
				if(count[j]<count[k])
					pos=k;	
			}
			temp=count[pos];
			count[pos]=count[j];
			count[j]=temp;
			stemp=words[pos];
			words[pos]=words[j];
			words[j]=stemp;
		}
	}
       //predict words from given next up to end of trie chain
       public void Traverse(String word, int wordCount, Node temp) {
		if(temp.end==1){
			words[WordsNo]=word;
			count[WordsNo]=temp.position;
			//System.out.println(s[index]+":"+position[index]+":"+index);
			WordsNo++;
		}
		if(temp!=null && WordsNo<wordCount){
			for(int j=0;j<256;j++){
				if(temp.next[j]!=null)
					Traverse(word+(char)j, wordCount, temp.next[j]);
			}
		}
	}
       
       //main
       public static void main(String[] args){
            try{
		Main trie=new Main("Dictionary.txt");
	
			}
            catch(Exception e){
                e.printStackTrace();
            }
        }
}
class Node {
	Node[] next=new Node[256];
	int position;
	int end;
}

