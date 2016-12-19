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
		if (!pasta.exists()){ //Se pasta n�o existe, cria
			System.out.println("O caminho indicado por \'Caminho Pasta\' n�o existe.");
		}

		//lista os arquivos
		File[] arquivos = pasta.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});

		// o arquivo zero � o readme
		for (int i = 1; i < arquivos.length; i++){
			nomeArquivo = (arquivos[i]).getName(); //obt�m o nome do arquivo
			//System.out.println(nomeArquivo);

			//arquivo de leitura a partir do caminhoPasta (string)
			try {
				fr = new FileReader(arquivos[i].getPath());
				//cria-se o buffer para ler do arquivo
				br = new BufferedReader(fr);

				//a entrada � apenas uma linha mesmo
				linhaEntrada = br.readLine();
				//remove-se os espa�os em branco do in�cio e fim da entrada
				linhaEntrada = linhaEntrada.trim();
				//possui as consultas a serem convertidas
				consultasEntrada = linhaEntrada.split(" ");							

				br.close();
				fr.close();

				//System.out.println(nomeArquivo);
				//System.out.println(textoEntrada);
				
				//aqui se far� o procedimento
				//come�a do 1 pq o 0 � a cabe�a da regra
				for (int j = 0; j < consultasEntrada.length; j ++){
					System.out.println(j + " " + consultasEntrada[j]);
					//possui as consultas por regras, que s�o separados por v�rgula
					//essas partes no padr�o ser�o removidos, mas para convers�o, n�o ser�o 
					//necess�rios, logo n�o ter� problema
					partesRegras = consultasEntrada[j].split("\\),");
					
					//come�a do 1 pq  cabe�a � desconsiderada
					for (int k = 1; k < partesRegras.length; k++)
						System.out.println(partesRegras[k]);
					System.out.println();
				}

			} catch (FileNotFoundException e) {
				System.out.println("Arquivo n�o encontrado.");
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
}