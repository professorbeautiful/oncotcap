package oncotcap.util;

import java.lang.reflect.*;

/**
 **	Helper functions for working with matrices
 **/
public class MatrixMath
{
	private static boolean [][] EMPTY_BOOLEAN_MATRIX = {};
	private static int [][] EMPTY_INT_MATRIX = {};
	
	/**
	 **	returns the result of A * B
	 **	treats true as 1, false as 0 (ANDs the elements)
	 **	the number of columns in A must equal the number of columns in
	 **	B, otherwise an empty matrix is returned.
	 **/
	public static boolean [][] multiply( boolean [][] a, boolean [][] b)
	{
		if(a.length > 0 && b.length > 0)
		{
			int colsA = a[0].length;
			int rowsA = a.length;
			int colsB = b[0].length;
			int rowsB = b.length;

			if (colsA == rowsB)
			{
				int [] arrayDims = {rowsA, colsB};
				boolean [][] c = (boolean [][]) Array.newInstance(boolean.class, arrayDims);

				boolean sum;
				
				for(int i = 0; i < rowsA; i++)
				{
					for(int j = 0; j < colsB; j++)
					{
						sum = true;
						for(int k = 0; k < colsA; k++)
							sum = sum && (a[i][k] && b[k][j]);
						c[i][j] = sum;
					}
				}
				return(c);
			}
		}
		return(EMPTY_BOOLEAN_MATRIX);
	}
	/**
	 **	returns the result of A * B
	 **	the number of columns in A must equal the number of rows in
	 **	B, otherwise an empty matrix is returned.
	 **/
	public static int [][] multiply( int [][] a, int [][] b)
	{
		if(a.length > 0 && b.length > 0)
		{
			int colsA = a[0].length;
			int rowsA = a.length;
			int colsB = b[0].length;
			int rowsB = b.length;

			if (colsA == rowsB)
			{
				int [] arrayDims = {rowsA, colsB};
				int [][] c = (int [][]) Array.newInstance(int.class, arrayDims);

				int sum;

				for(int i = 0; i < rowsA; i++)
				{
					for(int j = 0; j < colsB; j++)
					{
						sum = 0;
						for(int k = 0; k < colsA; k++)
							sum = sum + a[i][k] * b[k][j];
						c[i][j] = sum;
					}
				}
				return(c);
			}
		}
		return(EMPTY_INT_MATRIX);
	}

	/**
	 ** returns A + B
	 ** if A and B aren't the same size an empty matrix is returned
	 ** treats true as 1, false as 0 (ORs the elements together)
	 **/
	public static boolean [][] add(boolean [][] a, boolean [][] b)
	{
		if(a.length > 0 && b.length > 0)
		{
			int rowsA = a.length;
			int rowsB = b.length;
			int colsA = a[0].length;
			int colsB = b[0].length;
			if(rowsA == rowsB && colsA == colsB)
			{
				int [] arrayDims = {rowsA, colsA};
				boolean [][] c = (boolean [][]) Array.newInstance(boolean.class, arrayDims);
				for(int row = 0; row < rowsA; row++)
					for(int col = 0; col < colsA; col++)
						c[row][col] = a[row][col] || b[row][col];
				return(c);
			}
		}
		return(EMPTY_BOOLEAN_MATRIX);		
	}
	
	/**
	 ** returns A + B
	 ** if A and B aren't the same size an empty matrix is returned
	 **/
	public static int [][] add(int [][] a, int [][] b)
	{
		if(a.length > 0 && b.length > 0)
		{
			int rowsA = a.length;
			int rowsB = b.length;
			int colsA = a[0].length;
			int colsB = b[0].length;
			if(rowsA == rowsB && colsA == colsB)
			{
				int [] arrayDims = {rowsA, colsA};
				int [][] c = (int [][]) Array.newInstance(int.class, arrayDims);
				for(int row = 0; row < rowsA; row++)
					for(int col = 0; col < colsA; col++)
						c[row][col] = a[row][col] + b[row][col];
				return(c);
			}
		}
		return(EMPTY_INT_MATRIX);		
	}

	/**
	 **	makes a copy of the target matrix
	 **/
	public static int [][] copy(int [][] mat)
	{
		if(mat.length > 0 && mat[0].length > 0)
		{
			int rows = mat.length;
			int cols = mat[0].length;
			int [] arrayDims = {rows, cols};
			int [][] cpy = (int [][]) Array.newInstance(int.class, arrayDims);

			for(int r = 0; r < rows; r++)
				for(int c = 0; c < cols; c++)
					cpy[r][c] = mat[r][c];

			return(cpy);
		}
		else
			return(EMPTY_INT_MATRIX);
	}

	/**
	 **	converts a matrix of integer values to boolean values.
	 **	Nonzero elements are converted to true, elements that are zero
	 **	are converted to false.
	 **/
	public static boolean [][] toBoolean(int [][] mat)
	{
		if(mat.length > 0 && mat[0].length > 0)
		{
			int rows = mat.length;
			int cols = mat[0].length;
			int [] arrayDims = {rows, cols};
			boolean [][] cpy = (boolean [][]) Array.newInstance(boolean.class, arrayDims);

			for(int r = 0; r < rows; r++)
				for(int c = 0; c < cols; c++)
					cpy[r][c] = (mat[r][c] != 0);

			return(cpy);
		}
		else
			return(EMPTY_BOOLEAN_MATRIX);
	}

	/**
	 **	converts a matrix of boolean values to integer values.  True
	 **	elements are converted to 1, false elements are converted to 0
	 **/
	public static int [][] toInt(boolean [][] mat)
	{
		if(mat.length > 0 && mat[0].length > 0)
		{
			int rows = mat.length;
			int cols = mat[0].length;
			int [] arrayDims = {rows, cols};
			int [][] cpy = (int [][]) Array.newInstance(int.class, arrayDims);

			for(int r = 0; r < rows; r++)
				for(int c = 0; c < cols; c++)
					cpy[r][c] = (mat[r][c]) ? 1 : 0;

			return(cpy);
		}
		else
			return(EMPTY_INT_MATRIX);
	}

	/**
	 **	returns true if matrix contains only zero values in the upper
	 **	right triangle (above the diagonal) otherwize returns false.
	 **/
	public static boolean isLowerTriangularized(int [][] matrix)
	{
		int rows = matrix.length;
		if(rows > 0)
		{
			int columns = matrix[0].length;
			int row, column;
			for(row = 0; row < Math.min(rows, columns); row++)
				for(column = row + 1; column < columns; column++)
					if(matrix[row][column] != 0)
						return(false);
		}
		return(true);
	}

	/**
	 **	Zeros the diagonal of a matrix.  The target matrix must be
	 **	square.
	 **/
	public static int[][] zeroDiagonal(int [][] mat)
	{
		//only do this if the matrix isn't empty and is square
		if(mat.length > 0 && mat[0].length == mat.length)
		{
			int rows = mat.length;
			int cols = mat[0].length;
			int [] arrayDims = {rows, cols};
			int [][] cpy = copy(mat);

			for(int r = 0; r < rows; r++)
					cpy[r][r] = 0;

			return(cpy);
		}
		else
			return(EMPTY_INT_MATRIX);

	}
	public static int[][] toOnes(int [][] mat)
	{
		int rows = mat.length;
		int cols = mat[0].length;
		int [][] cpy = copy(mat);
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
			{
				if(cpy[r][c] > 0)
					cpy[r][c] = 1;
				else if(cpy[r][c] < 0)
					cpy[r][c] = -1;
			}
		return(cpy);
	}

}