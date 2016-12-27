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
		String consulta;
		String textoSaida = null;
		String[] partesRegras;

		//Buffer
		FileReader fr;
		BufferedReader br;
		FileWriter fw;
		BufferedWriter bw;
		
		//Caminho de saída
		File caminhoSaida = new File("02_Rule_files\\Consultas");
		caminhoSaida.mkdir();
		
		//System.out.println(arquivoPasta);
		if (!pasta.exists()) { //Se pasta não existe, cria
			System.out.println("O caminho indicado por \'Caminho Pasta\' não existe.");
		}
		
		// Iniciar dicionario de predicados
		predicateMapping = new HashMap<>();
		// Entidades
		predicateMapping.put("chunk", "cin:isChunk");
		predicateMapping.put("token", "cin:isToken");
		// Predicados envolvendo chaves relativas as entidades
		predicateMapping.put("ck_hasSucc", "cin:hasSucc");
		predicateMapping.put("ck_has_tokens", "cin:hasToken");
		predicateMapping.put("ck_hasHead", "cin:hasHead");
		predicateMapping.put("t_next", "cin:hasNext");
		// Predicados ou propriedades envolvendo uma entidade e seu valor
		predicateMapping.put("t_stem", "cin:hasStem");
		predicateMapping.put("t_bigPosAft", "cin:hasBigPosAft");
		predicateMapping.put("t_bigPosBef", "cin:hasBigPosBef");
		predicateMapping.put("t_trigPosAft", "cin:hasTrigPosAft");
		predicateMapping.put("t_trigPosBef", "cin:hasTrigPosBef");
		predicateMapping.put("t_ne_type", "cin:hasNEType");
		predicateMapping.put("t_mtype", "cin:hasMType");
		predicateMapping.put("t_subtype", "cin:hasSubType");
		predicateMapping.put("t_isHeadPP", "cin:isHeadPP");
		predicateMapping.put("t_isHeadNP", "cin:isHeadNP");
		predicateMapping.put("t_isHeadVP", "cin:isHeadVP");
		predicateMapping.put("ck_hasType", "cin:hasType");
		predicateMapping.put("t_pos", "cin:hasPos");
		predicateMapping.put("t_type", "cin:hasTkType");
		predicateMapping.put("t_root", "cin:isRoot");
		predicateMapping.put("t_ck_ot", "cin:hasCkType");
		predicateMapping.put("t_ck_tag_ot", "cin:hasCkTypeOT");
		predicateMapping.put("t_orth", "cin:hasOrth");
		predicateMapping.put("t_ner", "cin:hasNER");
		predicateMapping.put("t_gpos", "cin:hasGPos");
		predicateMapping.put("t_length", "cin:hasLength");
		predicateMapping.put("ck_posRelPred", "cin:hasDistToRoot");

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

				System.out.println(nomeArquivo);

				for (int j = 0; j < consultasEntrada.length; j ++){
					System.out.println(j + " " + consultasEntrada[j]);
					//possui as consultas por regras, que são separados por vírgula
					//essas partes no padrão serão removidos, mas para conversão, não serão 
					//necessários, logo não terá problema
					partesRegras = consultasEntrada[j].split("\\),");
					
					consulta = "";
					textoSaida = "";
					
					for (int k = 0; k < partesRegras.length; k++) {
						String[] partes = splitRegra(partesRegras[k]);
						if (k == 0) { // váriaveis da cabeça
							consulta += String.format("\n\nPREFIX cin: <http://www.ace.com/2005/>\nSELECT DISTINCT ?%s ?%s WHERE { ", partes[1], partes[2]);
						} else {
							//remove o ) caso tenha na regra
							partes[1] = partes[1].replace(")", "");
							partes[2] = partes[2].replace(")", "");
							if (partes[2].equals("true") || partes[2].charAt(0) == partes[2].toLowerCase().charAt(0))
								consulta += String.format("?%s %s \"%s\". ", partes[1], partes[0], partes[2]);
							else
								consulta += String.format("?%s %s ?%s. ", partes[1], partes[0], partes[2]);
						}
					}
					consulta += " }";
					textoSaida += consulta;
					System.out.println(consulta);
					System.out.println();
				}

			} catch (FileNotFoundException e) {
				System.out.println("Arquivo não encontrado.");
			} catch (IOException e) {
				System.out.println("Erro na leitura do arquivo.");
			}

			try {
				fw = new FileWriter(new File(caminhoSaida + "\\\\" + nomeArquivo + "_saida"));
				bw = new BufferedWriter(fw);
				bw.write(textoSaida); //por enquanto de entrada, mas trocar para o de saida
				bw.flush();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	private static String[] splitRegra(String regra) {
		String[] cabecaCorpo = regra.split("\\(");
		String[] partesCorpo;
		
		String[] partes = new String[3];
		
		partesCorpo = cabecaCorpo[1].split(",");
		if (partesCorpo.length == 3){
			partes[0] = "cin:has" + partesCorpo[0].toUpperCase().charAt(0)
					+ partesCorpo[0].substring(1);
		} else {
			partes[0] = predicateMapping.containsKey(cabecaCorpo[0]) ? predicateMapping.get(cabecaCorpo[0]) : cabecaCorpo[0];
		}

		if (partesCorpo.length == 1){//se há apenas um argumento
			partes[1] = partesCorpo[0];
			partes[2] = "true";
		} else if (partesCorpo.length == 2){ //se há dois arguementos na regra
			partes[1] = partesCorpo[0];
			//quando for hasGPos tem de adicionar um gen_ antes do atributo
			partes[2] = partes[0] == "hasGPos"? "gen_" + partesCorpo[1] : partesCorpo[1];
		} else { //se há 3 argumentos
			partes[1] = partesCorpo[1];
			partes[2] = partesCorpo[2];
		}

		return partes;
	}
}