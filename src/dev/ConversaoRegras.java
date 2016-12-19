/**
 * 
 */
package dev;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author JN
 *
 */
public class ConversaoRegras {

	/**
	 * @param args
	 */
	
	public static Map<String, String> predicateMapping;

	public static void main(String[] args) {
		String caminhoPasta = "02_Rule_files";
		File pasta = new File(caminhoPasta);

		String nomeArquivo;
		String[] consultasEntrada;
		String linhaEntrada = "";
		String textoSaida;
		String[] partesRegras;

		//Buffer
		FileReader fr;
		BufferedReader br;
		FileWriter fw;
		BufferedWriter bw;
		//System.out.println(arquivoPasta);
		if (!pasta.exists()) { //Se pasta não existe, cria
			System.out.println("O caminho indicado por \'Caminho Pasta\' não existe.");
		}
		
		// Iniciar dicionario de predicados
		predicateMapping = new HashMap<>();
		// Entidades
		predicateMapping.put("chunk", "isChunk");
		predicateMapping.put("token", "isToken");
		// Predicados envolvendo chaves relativas as entidades
		predicateMapping.put("ck_hasSucc", "hasSucc");
		predicateMapping.put("ck_has_tokens", "hasToken");
		predicateMapping.put("ck_hasHead", "hasHead");
		predicateMapping.put("t_next", "hasNext");
		// Predicados ou propriedades envolvendo uma entidade e seu valor
		predicateMapping.put("t_stem", "hasStem");
		predicateMapping.put("t_bigPosAft", "hasBigPosAft");
		predicateMapping.put("t_bigPosBef", "hasBigPosBef");
		predicateMapping.put("t_trigPosAft", "hasTrigPosAft");
		predicateMapping.put("t_trigPosBef", "hasTrigPosBef");
		predicateMapping.put("t_ne_type", "hasNEType");
		predicateMapping.put("t_mtype", "hasMType");
		predicateMapping.put("t_subtype", "hasSubType");
		predicateMapping.put("t_isHeadPP", "isHeadPP");
		predicateMapping.put("t_isHeadNP", "isHeadNP");
		predicateMapping.put("t_isHeadVP", "isHeadVP");
		predicateMapping.put("ck_hasType", "hasType");
		predicateMapping.put("t_pos", "hasPos");
		predicateMapping.put("t_type", "hasTkType");
		predicateMapping.put("t_ck_ot", "hasCkType");
		predicateMapping.put("t_ck_tag_ot", "hasCkTypeOT");
		predicateMapping.put("t_orth", "hasOrth");
		predicateMapping.put("t_ner", "hasNER");
		predicateMapping.put("t_gpos", "hasGPos");
		predicateMapping.put("t_length", "hasLength");
		predicateMapping.put("ck_posRelPred", "hasDistToRoot");

		//lista os arquivos
		File[] arquivos = pasta.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});

		// o arquivo zero é o readme
		for (int i = 1; i < arquivos.length; i++){
			nomeArquivo = (arquivos[i]).getName(); //obtém o nome do arquivo
			//System.out.println(nomeArquivo);

			//arquivo de leitura a partir do caminhoPasta (string)
			try {
				fr = new FileReader(arquivos[i].getPath());
				//cria-se o buffer para ler do arquivo
				br = new BufferedReader(fr);

				//a entrada é apenas uma linha mesmo
				linhaEntrada = br.readLine();
				//remove-se os espaços em branco do início e fim da entrada
				linhaEntrada = linhaEntrada.trim();
				//possui as consultas a serem convertidas
				consultasEntrada = linhaEntrada.split(" ");							

				br.close();
				fr.close();

				//System.out.println(nomeArquivo);
				//System.out.println(textoEntrada);
				
				//aqui se fará o procedimento
				//começa do 1 pq o 0 é a cabeça da regra
				for (int j = 0; j < consultasEntrada.length; j ++){
					System.out.println(j + " " + consultasEntrada[j]);
					//possui as consultas por regras, que são separados por vírgula
					//essas partes no padrão serão removidos, mas para conversão, não serão 
					//necessários, logo não terá problema
					partesRegras = consultasEntrada[j].split("\\),");
					
					String consulta = "";
					
					for (int k = 0; k < partesRegras.length; k++) {
						String[] partes = splitRegra(partesRegras[k]);
						if (k == 0) { // váriaveis da cabeça
							consulta += String.format("SELECT %s? %s? where { ", partes[1], partes[2]);
						} else {
							if (partes[2].equals("true"))
								consulta += String.format("%s? %s %s. ", partes[1], partes[0], partes[2]);
							else
								consulta += String.format("%s? %s %s?. ", partes[1], partes[0], partes[2]);
						}
					}
					consulta += " }";
					System.out.println(consulta);
					System.out.println();
				}

			} catch (FileNotFoundException e) {
				System.out.println("Arquivo não encontrado.");
			} catch (IOException e) {
				System.out.println("Erro na leitura do arquivo.");
			}

			textoSaida = "";
			try {
				fw = new FileWriter(new File(pasta + "\\\\" + nomeArquivo));
				bw = new BufferedWriter(fw);
				bw.write(linhaEntrada); //por enquanto de entrada, mas trocar para o de saida
				bw.flush();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	private static String[] splitRegra(String regra) {
		String[] parteInicio = regra.split("\\(");
		String[] partes = new String[3];
		
		partes[0] = predicateMapping.containsKey(parteInicio[0]) ? predicateMapping.get(parteInicio[0]) : parteInicio[0];
		partes[1] = parteInicio[1].split(",")[0];

		if (regra.contains(",")) {
			String parteFinal = parteInicio[1].split(",")[1];
			int index = parteFinal.indexOf(')');
			partes[2] = index != -1 ? parteFinal.substring(0, index) : parteFinal;
		} else if (parteInicio.length == 2) {
			partes[2] = "true";
		}
		
		return partes;
	}
}