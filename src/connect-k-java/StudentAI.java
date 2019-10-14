import java.util.*;
//The following part should be completed by students.
//Students can modify anything except the class name and exisiting functions and varibles.
public class StudentAI extends AI 
{
    private String[][] strboard;
    private HashSet<int []> Moveset = new HashSet<>();
	//define move score
	private int empty;
	private int my_piece [] = new int [5];
	private int op_piece [] = new int [4];
	private int mess;

	public StudentAI(int col, int row, int k, int g)
	{
		super(col, row, k, g);
		// initialize score
		empty = 7;
		my_piece[0] = 35;
		my_piece[1] = 800;
		my_piece[2] = 15000;
		my_piece[3] = 800000;
		op_piece[0] = 20;
		op_piece[1] = 500;
		op_piece[2] = 4800;
		op_piece[3] = 300000;
		mess = 0;

		// initialize board
		strboard=new String[col][row];
		for(int i=0;i<col;i++){
			for(int j=0;j<row;j++){
				strboard[i][j]="+";
			}
		}

		// initialize legal move set
		// stored as x,y (col,row)
		if(g == 0){
			for(int i=0;i<col;i++){
				for(int j=0;j<row;j++){
					Moveset.add(new int []{i,j});
				}
			}
		}
		else{
			for(int i=0;i<col;i++){
				Moveset.add(new int[]{i,row-1});
			}
		}
	}

	public Move GetMove(Move move) {
		// update opponent's move
		if(move.row!=-1){
			int []opmove = updateboard(move.col,move.row);
			strboard[opmove[1]][opmove[0]]="x";
		}

		// update my move
		int mymove[] = findmaxmove();
		int []opmove = updateboard(mymove[0],mymove[1]);
		strboard[opmove[1]][opmove[0]]="o";

		//print();

		return new Move(mymove[0],mymove[1]);
	}

	// return x,y
	public int[] findmaxmove(){
		int maxscore = -1;
		int [] maxmove = new int [2];
		for(int[] i:Moveset){
			int score = calscore(i);
			if(score>maxscore){
				maxmove = i;
				maxscore = score;
			}
		}
		return maxmove;
	}

	// calculate score at the paticular move
	public int calscore(int [] move){
		int score = 0;
		// check row
		StringBuilder rows = new StringBuilder();
		for(int i=0;i<col;i++){
			rows.append(strboard[move[1]][i]);
		}
		score += calunit(extract(rows.toString(), move[0]));
		// check col
		StringBuilder cols = new StringBuilder();
		for(int i=0;i<row;i++){
			cols.append(strboard[i][move[0]]);
		}
		score += calunit(extract(cols.toString(), move[1]));
		// check diagonal
		String []diagonals = diagonal(move[0],move[1]);
		score += calunit(extract(diagonals[0], move[0]));
		score += calunit(extract(diagonals[1], move[0]));
		return score;
	}

	public ArrayList<String> extract(String s, int pos){
		ArrayList<String> list = new ArrayList<>();
		for(int i=0;i<s.length()-k+1;i++){
			if(pos>=i&&pos<i+k){
				list.add(s.substring(i,i+k));
			}
		}
		return list;
	}

	public int calunit(ArrayList<String> list){
		int score = 0;
		for(int i=0;i<list.size();i++){
			String s = list.get(i);
			if (s.contains("o")&&s.contains("x")){
				score += mess;
			}
			else if (!s.contains("o")&&!s.contains("x")){
				score += empty;
			}
			else if (s.contains("o")){
				// only contains o
				int count = -1;
				for(int j=0;j<s.length();j++){
					if (s.charAt(j)=='o'){
						count++;
					}
				}
				score += my_piece[count];
			}
			else if (s.contains("x")){
				// only contains x
				int count = -1;
				for(int j=0;j<s.length();j++){
					if (s.charAt(j)=='x'){
						count++;
					}
				}
				score += op_piece[count];
			}
		}
		return score;
	}

	// return two diagonals of the point
	// diagonal 0 is from original point [0][0]
	// diagonal 1 is from x=0, y=row
	public String[] diagonal(int x, int y){
		String []result = new String [2];
		StringBuilder sb = new StringBuilder();
		int startx;
		int starty;
		// diagonal 0
		int b=y-x;
		if(b>=0){
			startx=0;
			starty=b;
		}
		else{
			startx=-b;
			starty=0;
		}
		while(startx<col&&starty<row){
			sb.append(strboard[starty][startx]);
			startx++;
			starty++;
		}
		result[0] = sb.toString();
		sb = new StringBuilder();
		// diagonal 1
		b=x+y;
		if(b<=row-1){
			startx=0;
			starty=b;
		}
		else{
			startx=b-row+1;
			starty=row-1;
		}

		while(startx<col&&starty>=0){
			sb.append(strboard[starty][startx]);
			startx++;
			starty--;
		}
		result[1] = sb.toString();
		return result;
	}

	public int [] updateboard(int x, int y){
		// remove the move
		if(g == 0) {
			for (int[] i : Moveset) {
				if (i[0] == x && i[1] == y) {
					Moveset.remove(i);
					return i;
				}
			}
		}
		else{
			// there is gravity
			for (int[] i : Moveset) {
				if(i[0] == x) {
					Moveset.remove(i);
					// if not full
					if (i[1] > 0) {
						Moveset.add(new int[]{i[0], i[1] - 1});
					}
					return i;
				}
			}
		}
		return new int [2];
	}

	public void print(){
		for(int i=0;i<strboard.length;i++){
			for(int j=0;j<strboard[0].length;j++){
				System.out.print(strboard[i][j]+" ");
			}
			System.out.println();
		}
		for(int []i:Moveset) {
			System.out.print("("+i[0]+","+i[1]+") ");
		}
		System.out.println();
	}
}