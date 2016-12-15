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
		String caminhoPasta = "C:\\Users\\JN\\Downloads\\BD_Triple-Store_RDF (1)\\02_Rule_files";
		File pasta = new File(caminhoPasta);

		String nomeArquivo;
		String textoEntrada;
		String linhaEntrada;
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

		//
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

			textoEntrada = "";
			//Arquivo de leitura a partir do caminhoTemporario (string)
			try {
				fr = new FileReader(arquivos[i].getPath());
				//Cria-se o buffer para ler do arquivo
				br = new BufferedReader(fr);

				linhaEntrada = br.readLine();
				while(linhaEntrada != null){
					textoEntrada = textoEntrada + " " + linhaEntrada;
					linhaEntrada = br.readLine();
				}

				//at� aqui se tem todo o texto lido
				br.close();
				fr.close();

				
				System.out.println(nomeArquivo);
				System.out.println(textoEntrada);
				
				//est� sendo divido pelas v�rgulas
				partesRegras = textoEntrada.split("\\),");
				//a parte ), das regras sumir�o, mas como vai fazer a convers�o, � s�
				//levar isso em considera��o
				
				
				//aqui se faria o procedimento
				//come�a do 1 pq o 0 � a cabe�a da regra
				for (int j = 1; j < partesRegras.length; j ++){
					System.out.println(partesRegras[j]);
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
				bw.write(textoEntrada); //por enquanto de entrada, mas trocar para o de saida
				bw.flush();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}