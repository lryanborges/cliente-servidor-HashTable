package application;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import entity.Veiculo;
import exceptions.SameKeyException;
import exceptions.WrongInsertException;

// Este servidor é utiliza a estrutura de uma HashTable
public class Servidor {

	protected No<Veiculo>[] table;
	protected int mod = 23;
	private int fatorDeCarga = 0;

	public Servidor(int tamanho) {
		mod = tamanho;
		this.table = new No[mod];
	}

	public Servidor() {
		this.table = new No[mod];
	}

	int hash(long num) {
		return (int) (num % mod);
	}

	void inserir(long num, Veiculo novo) throws SameKeyException {
		int hashedNum = hash(num);

		if (table[hashedNum] == null) {
			table[hashedNum] = new No<Veiculo>(num, novo);
			writeLog("\tVeículo ID " + num + " adicionado.\n");
			fatorDeCarga();
		} else {
			No<Veiculo> no = table[hashedNum];
			while (no.getNext() != null) {
				if (no.getPkey() == num) {
					throw new SameKeyException(num);
				}
				no = no.getNext();
			}

			if (no.getPkey() != num) {
				No<Veiculo> novoNo = new No<Veiculo>(num, novo);
				novoNo.setNext(table[hashedNum]);
				table[hashedNum] = novoNo;
				writeLog("\tVeículo ID " + num + " adicionado.\n");
				fatorDeCarga();
			}
		}
	}

	No<Veiculo> alterar(long num, Veiculo valor) throws WrongInsertException {
		int hashedNum = hash(num);
		No<Veiculo> no = table[hashedNum];

		while (no != null) {
			if (no.getPkey() == num) {
				no.setValor(valor);
				return no;
			}

			no = no.getNext();
		}

		throw new WrongInsertException();
	}

	No<Veiculo> remover(long num) throws WrongInsertException {
		int hashedNum = hash(num);
		No<Veiculo> no = table[hashedNum];

		// se for o primeiro
		if (no.getPkey() == num) {
			table[hashedNum] = no.getNext();
			writeLog("\tVeículo ID " + num + " removido.\n");
			fatorDeCarga();
			return no;
		}

		while (no != null) {
			No<Veiculo> next = no.getNext();

			// se for o próximo
			if (next.getPkey() == num) {
				no.setNext(next.getNext());
				writeLog("\tVeículo ID " + num + " removido.\n");
				fatorDeCarga();
				return no;
			}

			no = no.getNext();
		}

		throw new WrongInsertException();
	}

	void printTable() {
		No<Veiculo> no;
		for (int i = 0; i < table.length; i++) {
			no = table[i];
			System.out.print("Linha " + i + ": ");

			while (no != null) {
				System.out.print(no.getValor().getRenavam() + " --> ");
				no = no.getNext();
			}

			System.out.println("null");
		}
	}

	void print() {
		No<Veiculo> no;
		for (int i = 0; i < table.length; i++) {
			no = table[i];

			while (no != null) {
				System.out.println("--------------------------------------------------");
				System.out.println("Placa: " + no.getValor().getPlaca());
				System.out.println("Renavam: " + no.getPkey());
				System.out.println("Condutor: " + no.getValor().getCondutor().getNome() + ", "
						+ no.getValor().getCondutor().getCpf());
				System.out.println("Modelo: " + no.getValor().getModelo());
				System.out.println("Data de Fabricação: " + no.getValor().getFabricacao());
				no = no.getNext();
			}
		}
	}

	int contarVeiculos() {
		int cont = 0;
		No<Veiculo> no;

		for (int i = 0; i < table.length; i++) {
			no = table[i];

			while (no != null) {
				cont++;
				no = no.getNext();
			}
		}

		return cont;
	}

	No<Veiculo> buscar(long num) {

		// hashedNum é o resto do número pelo modulo, meio que a linha da tabela que ele
		// vai ficar
		int hashedNum = hash(num);
		No<Veiculo> no = table[hashedNum];

		while (no != null) {
			if (no.getPkey() == num) {
				return no;
			}

			no = no.getNext();
		}

		return null; // quer dizer que não achou
	}
	
	void fatorDeCarga() {
		float fator = 0;
		DecimalFormat df = new DecimalFormat("#.###");
		
		fator = (float) contarVeiculos() / mod;
		
		writeLog("Fator de Carga: " + df.format(fator) + "\n");
	}
	
	boolean primo(long num) {
		int cont = 0;
		for(long i = num; i != 0; i--) {
			if(num % i == 0) {
				cont++;
			}
		}
		
		if(cont <= 2) {
			return true;
		} else {
			return false;	
		}
	}

	public void cleanLog() {
		String path = "src/log.txt";

		FileWriter writer;
		try {
			writer = new FileWriter(path, false);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeLog(String log) {
		String path = "src/log.txt";

		FileWriter writer;
		try {
			writer = new FileWriter(path, true);
			writer.write(log);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public No<Veiculo>[] getTable() {
		return table;
	}

	public void setTable(No<Veiculo>[] table) {
		this.table = table;
	}

	public int getMod() {
		return mod;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	public int getFatorDeCarga() {
		return fatorDeCarga;
	}

	public void setFatorDeCarga(int fatorDeCarga) {
		this.fatorDeCarga = fatorDeCarga;
	}

}
