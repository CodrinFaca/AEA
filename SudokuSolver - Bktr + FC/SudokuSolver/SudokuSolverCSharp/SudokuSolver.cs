using System;
using System.Collections.Generic;
using System.Text;
using System.Collections;

namespace SudokuSolverCSharp
{
  /// <summary>
  /// Sudoku solver based on forward checking
  /// IAIP Mandatory Assignment 4, Spring 2007
  /// Ported from SudokuSolver.java (by Tarik Hadzic and Mai Ajspur)
  /// 
  /// The method: public List<int> FC(List<int> asn) 
  /// is the only part we coded.
  /// </summary>
  public class SudokuSolver
  {
    private int[,] puzzle;
    private int size;
    private List<List<int>> D;

    public int[,] getPuzzle()
    {
      return puzzle;
    }

    public void setValue(int col, int row, int value)
    {
      puzzle[col, row] = value;
    }

    public void setup(int size1)
    {
      size = size1;
      puzzle = new int[size * size, size * size];
      D = new List<List<int>>(size * size * size * size);

      //Initialize each D[X]
      for (int i = 0; i < (size * size * size * size); i++)
      {
        List<int> l = new List<int>(9);
        for (int j = 1; j < 10; j++) l.Add(j);
        D.Add(l);
      }

    }


    public int[,] solve()
    {
      List<int> asn = GetAssignment(puzzle);

      //INITIAL_FC
      if (!INITIAL_FC(asn)) return null;

      //FC
      List<int> solution = FC(asn);

      if (solution == null) return null;
      return GetPuzzle(solution);
    }

    public void readInPuzzle(int[,] p)
    {
      puzzle = p;
    }

    private List<List<int>> CloneList(List<List<int>> l)
    {
      List<List<int>> rt = new List<List<int>>(l.Count);
      foreach (List<int> ll in l)
      {
        rt.Add(new List<int>(ll.ToArray()));
      }
      return rt;
    }

    //---------------------------------------------------------------------------------
    //YOUR TASK:  Implement FC(asn)
    //---------------------------------------------------------------------------------
    /// <summary>
    /// This is our implementation of the provided pseudocode 
    /// </summary>
    /// <param name="asn">List of assignments</param>
    /// <returns>A solution or null if none exists</returns>
    public List<int> FC(List<int> asn)
    {
      // If we have no 0's (empty squares) in the list of assignments,
      // we have a solution and nothing left to do - return immidiately!
      if (!asn.Contains(0))
      {
        return asn;
      }
      // Get the first zero, and begin solving that
      int X = asn.IndexOf(0);
      // In order to be able to roll back to a given point if we reach a dead end
      // we create a clone of the list.
      List<List<int>> Dold = CloneList(D);
      // Iterate over the current domain of the selected value to try and find a solution
      // As the domain is being modified we need to work on a copy of the domain. Thus the ToArray call
      foreach (int V in D[X].ToArray())
      {
        // Call AC_FC with the current value to see if it gives a consistant result
        if (AC_FC(X, V))
        {
          // The rules permit the number there so do that
          // then call FC recursively to try and find a solution with the number there.
          asn[X] = V;
          List<int> result = FC(asn);
          if (result != null)
          {
            // The call has returned a solution!
            // Return it up the tree and end the algorithm.
            return result;
          }
          // We have reached a dead end for the number, roll back.
          asn[X] = 0;
          D = CloneList(Dold);
        }
        else
        {
          // The number could not be placed at that slot. Roll back.
          D = CloneList(Dold);
        }
      }
      // There are no solutions with the current configuration. Return failure.
      return null;
    }




    //---------------------------------------------------------------------------------
    // CODE SUPPORT FOR IMPLEMENTING FC(asn)
    //
    // It is possible to implement FC(asn) by using only AC_FC function from below.
    // 
    // If you have time, I strongly reccomend that you implement AC_FC and REVISE from scratch
    // using only implementation of CONSISTENT algorithm and general utility functions. In my opinion
    // by doing this, you will gain much more from this exercise.
    //
    //---------------------------------------------------------------------------------



    //------------------------------------------------------------------
    //				AC_FC
    //
    // Implementation of AC-FC(cv) pseudo-code from B05 notes, p29.
    // This is a key component of FC algorithm, and the only function you need to 
    // use in your FC(asn) implementation
    //------------------------------------------------------------------
    public bool AC_FC(int X, int V)
    {
      //Reduce domain Dx
      D[X].Clear();
      D[X].Add(V);

      //Put in Q all relevant Y where Y>X
      Queue<int> Q = new Queue<int>(); //list of all relevant Y
      int col = GetColumn(X);
      int row = GetRow(X);
      int cell_x = row / size;
      int cell_y = col / size;

      //all variables in the same column
      for (int i = 0; i < size * size; i++)
      {
        if (GetVariable(i, col) > X)
        {
          Q.Enqueue(GetVariable(i, col));
        }
      }
      //all variables in the same row
      for (int j = 0; j < size * size; j++)
      {
        if (GetVariable(row, j) > X)
        {
          Q.Enqueue(GetVariable(row, j));
        }
      }
      //all variables in the same size*size box
      for (int i = cell_x * size; i <= cell_x * size + 2; i++)
      {
        for (int j = cell_y * size; j <= cell_y * size + 2; j++)
        {
          if (GetVariable(i, j) > X)
          {
            Q.Enqueue(GetVariable(i, j));
          }
        }
      }

      //REVISE(Y,X)
      bool consistent = true;
      while (!(Q.Count == 0) && consistent)
      {
        int Y = Q.Dequeue();
        if (REVISE(Y, X))
        {
          consistent = !(D[Y].Count == 0);
        }
      }
      return consistent;
    }


    //------------------------------------------------------------------
    //				REVISE 
    //------------------------------------------------------------------
    public bool REVISE(int Xi, int Xj)
    {
      int zero = 0;

      System.Diagnostics.Debug.Assert(Xi >= 0 && Xj >= 0);
      System.Diagnostics.Debug.Assert(Xi < size * size * size * size && Xj < size * size * size * size);
      System.Diagnostics.Debug.Assert(Xi != Xj);

      bool DELETED = false;

      List<int> Di = D[Xi];
      List<int> Dj = D[Xj];

      for (int i = 0; i < Di.Count; i++)
      {
        int vi = (int)Di[i];
        List<int> xiEqVal = new List<int>(size * size * size * size);
        for (int var = 0; var < size * size * size * size; var++)
        {
          xiEqVal.Insert(var, zero);
        }

        xiEqVal[Xi] = vi;

        bool hasSupport = false;
        for (int j = 0; j < Dj.Count; j++)
        {
          int vj = (int)Dj[j];
          if (CONSISTENT(xiEqVal, Xj, vj))
          {
            hasSupport = true;
            break;
          }
        }

        if (hasSupport == false)
        {
          Di.Remove((int)vi);
          DELETED = true;
        }

      }

      return DELETED;
    }




    //------------------------------------------------------------------
    //CONSISTENT: 
    //
    //Given a partiall assignment "asn"  checks whether its extension with 
    //variable = val is consistent with Sudoku rules, i.e. whether it violates
    //any of constraints whose all variables in the scope have been assigned. 
    //This implicitly encodes all constraints describing Sudoku.
    //
    //Before it returns, it undoes the temporary assignment variable=val
    //It can be used as a building block for REVISE and AC-FC
    //
    //NOTE: the procedure assumes that all assigned values are in the range 
    // 		{0,..,9}. 
    //-------------------------------------------------------------------
    public bool CONSISTENT(List<int> asn, int variable, int val)
    {
      int v1, v2;

      //variable to be assigned must be clear
      //assert(asn[variable] == 0);
      asn[variable] = val;

      //alldiff(col[i])
      for (int i = 0; i < size * size; i++)
      {
        for (int j = 0; j < size * size; j++)
        {
          for (int k = 0; k < size * size; k++)
          {
            if (k != j)
            {
              v1 = (int)asn[GetVariable(i, j)];
              v2 = (int)asn[GetVariable(i, k)];
              if (v1 != 0 && v2 != 0 && v1.CompareTo(v2) == 0)
              {
                asn[variable] = 0;
                return false;
              }
            }
          }
        }
      }



      //alldiff(row[j])
      for (int j = 0; j < size * size; j++)
      {
        for (int i = 0; i < size * size; i++)
        {
          for (int k = 0; k < size * size; k++)
          {
            if (k != i)
            {
              v1 = (int)asn[GetVariable(i, j)];
              v2 = (int)asn[GetVariable(k, j)];
              if (v1 != 0 && v2 != 0 && v1.CompareTo(v2) == 0)
              {
                asn[variable] = 0;
                return false;
              }
            }
          }
        }
      }


      //alldiff(block[size*i,size*j])
      for (int i = 0; i < size; i++)
      {
        for (int j = 0; j < size; j++)
        {
          for (int i1 = 0; i1 < size; i1++)
          {
            for (int j1 = 0; j1 < size; j1++)
            {
              int var1 = GetVariable(size * i + i1, size * j + j1);
              for (int i2 = 0; i2 < size; i2++)
              {
                for (int j2 = 0; j2 < size; j2++)
                {
                  int var2 = GetVariable(size * i + i2, size * j + j2);
                  if (var1 != var2)
                  {
                    v1 = (int)asn[var1];
                    v2 = (int)asn[var2];
                    if (v1 != 0 && v2 != 0 && v1.CompareTo(v2) == 0)
                    {
                      asn[variable] = 0;
                      return false;
                    }
                  }
                }
              }

            }
          }
        }
      }

      asn[variable] = 0;
      return true;
    }




    //------------------------------------------------------------------
    //						INITIAL_FC
    //------------------------------------------------------------------
    public bool INITIAL_FC(List<int> anAssignment)
    {
      //Enforces consistency between unassigned variables and all 
      //initially assigned values; 
      for (int i = 0; i < anAssignment.Count; i++)
      {
        int V = (int)anAssignment[i];
        if (V != 0)
        {
          Queue<int> Q = GetRelevantVariables(i);
          bool consistent = true;
          while (!(Q.Count == 0) && consistent)
          {
            int Y = Q.Dequeue();
            if (REVISE(Y, i))
            {
              consistent = !(D[Y].Count == 0);
            }
          }
          if (!consistent) return false;
        }
      }

      return true;
    }




    //------------------------------------------------------------------
    //						GetRelevantVariables
    //------------------------------------------------------------------
    public Queue<int> GetRelevantVariables(int X)
    {
      //Returns all variables that are interdependent of X, i.e. 
      //all variables involved in a binary constraint with X
      Queue<int> Q = new Queue<int>(); //list of all relevant Y
      int col = GetColumn(X);
      int row = GetRow(X);
      int cell_x = row / size;
      int cell_y = col / size;

      //all variables in the same column
      for (int i = 0; i < size * size; i++)
      {
        if (GetVariable(i, col) != X)
        {
          Q.Enqueue(GetVariable(i, col));
        }
      }
      //all variables in the same row
      for (int j = 0; j < size * size; j++)
      {
        if (GetVariable(row, j) != X)
        {
          Q.Enqueue(GetVariable(row, j));
        }
      }
      //all variables in the same size*size cell
      for (int i = cell_x * size; i <= cell_x * size + 2; i++)
      {
        for (int j = cell_y * size; j <= cell_y * size + 2; j++)
        {
          if (GetVariable(i, j) != X)
          {
            Q.Enqueue(GetVariable(i, j));
          }
        }
      }

      return Q;
    }





    //------------------------------------------------------------------
    // Functions translating between the puzzle and an assignment
    //-------------------------------------------------------------------
    public List<int> GetAssignment(int[,] p)
    {
      List<int> asn = new List<int>();
      int[] t_asn = new int[size * size * size * size];
      for (int i = 0; i < size * size; i++)
      {
        for (int j = 0; j < size * size; j++)
        {
          t_asn[GetVariable(i, j)] = p[i, j];
          if (p[i, j] != 0)
          {
            //restrict domain
            D[GetVariable(i, j)].Clear();
            D[GetVariable(i, j)].Add(p[i, j]);
          }
        }
      }
      asn.AddRange(t_asn);
      return asn;
    }


    public int[,] GetPuzzle(List<int> asn)
    {
      int[,] p = new int[size * size, size * size];
      for (int i = 0; i < size * size; i++)
      {
        for (int j = 0; j < size * size; j++)
        {
          int val = (int)asn[GetVariable(i, j)];
          p[i, j] = val;
        }
      }
      return p;
    }


    //------------------------------------------------------------------
    //Utility functions
    //-------------------------------------------------------------------
    public int GetVariable(int i, int j)
    {
      //System.Diagnostics.Debug.Assert(i < size * size && j < size * size);
      //System.Diagnostics.Debug.Assert(i >= 0 && j >= 0);
      return (i * size * size + j);
    }


    public int GetRow(int X)
    {
      return (X / (size * size));
    }

    public int GetColumn(int X)
    {
      return X - ((X / (size * size)) * size * size);
    }



  }
}
