package bri.honeywell;

import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import api.RFIDReader;
import api.Tag;

import com.intermec.datacollection.rfid.BasicBRIReader;
import com.intermec.datacollection.rfid.BasicReaderException;

import exceptions.RFIDConnectionProblem;
import exceptions.ReadTagException;

public class IF1Honeywell implements RFIDReader{
	
	public static final int NUMERO_RESULTADO = 2;
	
	BasicBRIReader mReader;
	
	public IF1Honeywell(String URI)throws RFIDConnectionProblem{
		mReader = new BasicBRIReader();
		 try {
			mReader.open(URI);
		} catch (BasicReaderException e) {
			e.printStackTrace();
			throw new RFIDConnectionProblem();
		}
		
	}

	@Override
	public boolean start() {
		try {
			mReader.execute("read tagid report=eventall");
		} catch (BasicReaderException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean stop() {
		try {
			mReader.execute("read stop");
			mReader.close();
		} catch (BasicReaderException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public Map<String, Tag> read() throws ReadTagException {
		Map<String, Tag> tagMap = new ConcurrentHashMap<>();
		try {
			String s = mReader.execute("read poll");
			StringTokenizer t = new StringTokenizer(s,"\n");
			while (t.hasMoreTokens()){ // tenho tag
				StringTokenizer t2 = new StringTokenizer(t.nextToken());
				int count = 0;
				while (t2.hasMoreTokens()){ // tenho elementos tag
					String tagID = null;
					if (count >= NUMERO_RESULTADO){ // se tiver mais doque os elementos buscados. ERRO!
						System.out.println(count);
						this.stop();
						throw new ReadTagException();
						
					}
					if (count == 0){  // n mecher aqui, ah n ser que queira pegar o primeiro campo
						t2.nextToken();
						count++;
						continue;
					}
					if (count == 1){ // elemento tagid
						tagID = t2.nextToken();
						if (erroLeitura(tagID)){ // se tem erro, contabiliza e continua
							count++;
							continue;
						}
					}

					// proximas informacoes da Tag vao entrar no constructor de TagImpl
					tagMap.put(tagID, new TagImpl(tagID));
					count++;
				}
			}
			
		} catch (BasicReaderException e) {
			e.printStackTrace();
			return null;
		}
		return tagMap;
	}
	
	private boolean erroLeitura(String s){
		Scanner ss = new Scanner(s);
		String sss = ss.findInLine("RDERR");
		
		if (sss != null){  // se encontrar RDERR eh pq tem erro, entao volta true
			return true;
		}
		return false; // false
	}

}
