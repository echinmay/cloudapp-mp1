import java.io.File;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MP1 {
	public class strval implements Comparable<strval> {
		public strval(String name, Integer value) {
			this.name = name;
			this.value = value;
		}
		public strval(String name) {
			this.name = name;
			this.value = 0;
		}
		private String name;
		private Integer value;
		//@Override 
		public int compareTo(strval other) {
			int i = (other.value).compareTo(this.value);
			if (i == 0) {
				return ((this.name).compareTo(other.name));
			}
			return i;
		}
		public void incrementCount() {
			this.value += 1;
		}
	}
	Map<String,strval> m = new HashMap<String,strval>();
	Map<String,Boolean> swords ;
	
	private void initswordsmap() {
		this.swords = new HashMap<String,Boolean>();
		for (String s : stopWordsArray) {
			this.swords.put(s, true);
		}
	}
	
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};
    
    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
        this.initswordsmap();
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        //TODO
        BufferedReader br = new BufferedReader(new FileReader(this.inputFileName));
        ArrayList<String> lines = new ArrayList<String>();
        
        String line = br.readLine();
        while(line != null) {
            lines.add(line);
            line = br.readLine();
        }
        Integer[] interestedIndices = this.getIndexes();
        for (Integer idx: interestedIndices) {
        	line = lines.get(idx);
        	StringTokenizer st = new StringTokenizer(line, delimiters);
            while(st.hasMoreTokens()) {
                String tok = st.nextToken().toLowerCase().trim();
                if (swords.containsKey(tok)) { continue; }
                if (m.containsKey(tok)) {
                	m.get(tok).incrementCount();
                } else {
                	strval newpair = new strval(tok);
                	m.put(tok, newpair);
                }
            }       
        }
        List<strval> strvallist = new ArrayList<strval>(m.values());
        Collections.sort(strvallist);
        for (int j = 0; j < 20; j++)
        {
        	//System.out.println(strvallist.get(j).name + strvallist.get(j).value);
        	ret[j] = strvallist.get(j).name;
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
