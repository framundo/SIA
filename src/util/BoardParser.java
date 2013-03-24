package util;

import gps.SearchStrategy;
import gps.api.GpsProblem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Board;
import model.DeepTripProblem;
import model.DeepTripProblem.Heuristic;
import model.EngineImpl;

public class BoardParser {

	public static List<Board> parseBoards(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s;
		List<Board> boards = new LinkedList<Board>();
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
						br.close();
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
		br.close();
		return boards;
	}

	public static Board parseBoard(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s;
		while((s=br.readLine())!=null){
			ArrayList<int[]> tiles = null;
			int rowNumber = 0;
			int colors = 0;
			while(s!=null && s.length()!=0){
				String[] row = s.split(",");
				int size = row.length-1;
				if(rowNumber==0){
					tiles=new ArrayList<int[]>();
				}else{
					if(size != tiles.get(0).length){
						br.close();
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
			br.close();
			return new Board(tiles.toArray(new int[0][0]), colors);
		}
		br.close();
		return null;
	}

	public static void main(String[] args) throws IOException {
		List<Board> boards = BoardParser.parseBoards(new File("testBoards/5x5boards"));
		StatisticData average = new StatisticData();
		int count = 0;
		for(SearchStrategy s: SearchStrategy.values()){
			for(Heuristic h: Heuristic.values()){
				if(h.equals(Heuristic.NONE)) break;
				System.out.println(s+" "+h);
				for(Board board: boards){
					GpsProblem problem = new DeepTripProblem(board, h);
					StatisticData answer = (new EngineImpl()).engine(problem, s);
					if (answer !=null) {
						//System.out.println((count++)+" time:"+answer);
						average.time += answer.time;
						average.height += answer.height;
						average.nodes += answer.nodes;
					}
				}
				System.out.println("Time: "+average.time/boards.size());
				System.out.println("Nodes: "+average.nodes/boards.size());
				System.out.println("Height: "+average.height/boards.size());
				System.out.println("");
				if(s.equals(SearchStrategy.DFS)||s.equals(SearchStrategy.BFS)||s.equals(SearchStrategy.ID)){
					break;
				}
			}
		}
	}
}
