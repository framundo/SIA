package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import model.Board;

public class BoardParser {
	
	public static Collection<Board> parse(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s;
		Set<Board> boards = new HashSet<Board>();
		while((s=br.readLine())!=null){
			ArrayList<int[]> tiles = null;
			int rowNumber = 0;
			int colors = 0;
			while(s.length()!=0){
				String[] row = s.split(",");
				int size = row.length-1;
				if(rowNumber==0){
					tiles=new ArrayList<int[]>();
				}else{
					if(size != tiles.get(0).length){
						throw new IOException();
					}
				}
				tiles.add(new int[size]);
				for(int i=0; i<size; i++){
					int color = Integer.valueOf(row[i].trim());
					tiles.get(rowNumber)[i] = color;		
					if(color>colors){
						colors = color;
					}
				}
				rowNumber++;
				s = br.readLine();
			}
			boards.add(new Board(tiles.toArray(new int[0][0]), colors));
		}
		return boards;
	}

	
	public static void main(String[] args) throws IOException {
		Collection<Board> boards = BoardParser.parse(new File("testBoards/5x5boards"));
		for(Board board: boards){
			System.out.println(board);
		}
	}
}
