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

/**
 * @author JN
 *
 */
public class ConversaoRegras {

	/**
	 * @param args
	 */

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
					
					//começa do 1 pq  cabeça é desconsiderada
					for (int k = 0; k < partesRegras.length; k++) {
						String[] partes = splitRegra(partesRegras[k]);
						if (k == 0) { // váriaveis da cabeça
							consulta += String.format("SELECT %s? %s? where { ", partes[1], partes[2]);
						} else if (partes.length == 3) {
							consulta += String.format("%s? %s %s?. ", partes[1], partes[0], partes[2]);
						} else {
							// TODO: algumas consultas são: preposicao(A).
							// Como montar as triplas com ela?
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
		String[] partes = null;
		if (regra.contains(",")) {
			partes = new String[3];
			partes[0] = parteInicio[0];
			partes[1] = parteInicio[1].split(",")[0];
			partes[2] = parteInicio[1].split(",")[1].substring(0, parteInicio[1].split(",")[1].length() - 1);
		} else if (parteInicio.length == 2) {
			partes = new String[2];
			partes[0] = parteInicio[0];
			partes[1] = parteInicio[1].split(",")[0];
		}
		
		return partes;
	}
}