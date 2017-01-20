using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SudokuSolver
{
  public class SolveSudoku
  {
    private int[][] Squares;
    private int _size;
    private List<int> _allPossible = new List<int>() ;
    private List<CollectionRemaining> _rows { get; set; }// = new List<CollectionRemaining>();
    private List<CollectionRemaining> _columns { get; set; }// = new List<CollectionRemaining>();
    private List<CollectionRemaining> _innerSquares { get; set; }// = new List<CollectionRemaining>();
    //private Dictionary<int, List<int>> _rowsToRevise = new Dictionary<int, List<int>>(); // revise row by removing values
    //private Dictionary<int, List<int>> _columnsToRevise = new Dictionary<int, List<int>>();
    //private Dictionary<int, List<int>> _squaresRevise = new Dictionary<int, List<int>>();
    private Dictionary<int, bool> _queueDictionary = new Dictionary<int, bool>();

    private Queue<ItemToReevaluate> _reEvaluationQueue = new Queue<ItemToReevaluate>();

    private Dictionary<int, UndeterminedSquare> _undeterminedList = new Dictionary<int, UndeterminedSquare>();
    public SolveSudoku(int size, int[][] input)
    {
      this.Squares = input;
      _size = size;
      for (int i = 1; i <= size; i++)
        _allPossible.Add(i);

        //initialize fields:
        _rows = new List<CollectionRemaining>();
        _columns = new List<CollectionRemaining>();
        _innerSquares = new List<CollectionRemaining>();

    }

    private void FillRemainingCollections()
    {
      //TODO: split this into threads

      for (int i = 0; i < _size; i++)
      {
        CollectionRemaining c = new CollectionRemaining
        {
          RemainingValues = _allPossible.Except(Squares[i].Where(x => x != 0)).ToList()
        };
        _rows.Add(c);
      }
      //Get undetermined columns
      for (int i = 0; i < _size; i++)
      {
        List<int> existing = new List<int>();
        for (int j = 0; j < _size; j++)
        {
          if (Squares[j][i] != 0)
            existing.Add(Squares[j][i]);
        }
        CollectionRemaining c = new CollectionRemaining
        {
          RemainingValues = _allPossible.Except(existing).ToList()
        };
        _columns.Add(c);
      }
      //Get undetermined inner squares (the 3x3)
      for (int i = 0; i < 3; i++)
      {
        for (int j = 0; j < 3; j++)
        {
         
          List<int> existing = new List<int>();
          if (Squares[i * 3 + 0][j * 3 + 0] != 0)
            existing.Add(Squares[i * 3 + 0][j * 3 + 0]);
          if (Squares[i * 3 + 1][j * 3 + 0] != 0)
            existing.Add(Squares[i * 3 + 1][j * 3 + 0]);
          if (Squares[i * 3 + 2][j * 3 + 0] != 0)
            existing.Add(Squares[i * 3 + 2][j * 3 + 0]);
          if (Squares[i * 3 + 0][j * 3 + 1] != 0)
            existing.Add(Squares[i * 3 + 0][j * 3 + 1]);
          if (Squares[i * 3 + 1][j * 3 + 1] != 0)
            existing.Add(Squares[i * 3 + 1][j * 3 + 1]);
          if (Squares[i * 3 + 2][j * 3 + 1] != 0)
            existing.Add(Squares[i * 3 + 2][j * 3 + 1]);
          if (Squares[i * 3 + 0][j * 3 + 2] != 0)
            existing.Add(Squares[i * 3 + 0][j * 3 + 2]);
          if (Squares[i * 3 + 1][j * 3 + 2] != 0)
            existing.Add(Squares[i * 3 + 1][j * 3 + 2]);
          if (Squares[i * 3 + 2][j * 3 + 2] != 0)
            existing.Add(Squares[i * 3 + 2][j * 3 + 2]);

          CollectionRemaining sq = new CollectionRemaining
          {
            RemainingValues = _allPossible.Except(existing).ToList()
          };
          _innerSquares.Add(sq);
        }
      }
    }

    private void ProcessFollowingWaves()
    {
     
    }

    private void CreateSavepoint(UndeterminedSquare wildElem)
    {
      //TODO: save undetermined list (hard copy, and save wild elem on a Queue
      throw new NotImplementedException();
    }
    public int[][] StartProcessing()
    {
      //Get undetermined rows
      FillRemainingCollections();

      //stop condition: no undetermined squares

      FirstStep();

      while(_undeterminedList.Count > 0)
      {
        //TODO: process reEvaluate Queue and remove elements
        var cantDecide = true;
        int fewest = _size;
        UndeterminedSquare wild = null;
        while (_reEvaluationQueue.Count > 0)
        {
            if (_undeterminedList.Count == 0)
                break;
            var toReeval = _reEvaluationQueue.Dequeue();
            switch (toReeval.Type)
            {
                case ItemType.ROW:
                    {
                        //check to see if row actually has possible elements:
                        if (_rows[toReeval.Number].RemainingValues.Count == 0)
                            break;
                        //mark that this item was processed: 
                        ProcessRow(ref cantDecide, fewest, ref wild, toReeval);
                        break;
                    }
                case ItemType.COLUMN:
                    {
                        //check to see if row actually has possible elements:
                        if (_columns[toReeval.Number].RemainingValues.Count == 0)
                            break;
                        ProcessColumn(ref cantDecide, fewest, ref wild, toReeval);
                        break;
                    }

                case ItemType.SQUARE:
                    {
                        //check to see if row actually has possible elements:
                        if (_innerSquares[toReeval.Number].RemainingValues.Count == 0)
                            break;
                        ProcessSquare(ref cantDecide, fewest, ref wild, toReeval);
                        break;
                    }
            }
        }

      }
      if (_undeterminedList.Count > 0)
      {
          //Do that guessing magic
      }
      return Squares;
    }

    private void ProcessSquare(ref bool cantDecide, int fewest, ref UndeterminedSquare wild, ItemToReevaluate toReeval)
    {
        int queueSquare = ("square" + toReeval.Number).GetHashCode();
        _queueDictionary[queueSquare] = false;
        //iterat through all items in square
        int startRow = (toReeval.Number / 3) * 3;
        int startCol = (toReeval.Number % 3) * 3;
        for (int i = startRow; i < startRow + 3; i++)
        {
            for (int j = startCol; j < startCol + 3; j++)
            {
                int position = i * _size + j;
                if (_undeterminedList.ContainsKey(position))
                {
                    var unSq = _undeterminedList[position];
                    var possible = ComputePossible(i, j, toReeval.Number);
                    if (possible.Count == 1)
                    {
                        cantDecide = false;
                        var winner = possible[0];
                        _undeterminedList.Remove(position);
                        Squares[i][j] = winner;
                        Reevaluate(i, j, toReeval.Number, winner);
                    }
                    else
                    {
                        if (possible.Count < fewest)
                            wild = unSq;
                        unSq.Possible = possible;
                    }
                }
            }
        }
    }

    private void ProcessColumn(ref bool cantDecide, int fewest, ref UndeterminedSquare wild, ItemToReevaluate toReeval)
    {

        //mark that this item was processed: 
        int queueColumn = ("column" + toReeval.Number).GetHashCode();
        _queueDictionary[queueColumn] = false;
        //iterate through all items in column
        for (int i = 0; i < _size; i++)
        {
            int position = i * _size + toReeval.Number;
            if (_undeterminedList.ContainsKey(position))
            {
                int innerSquare = GetInnerSquare(i, toReeval.Number);
                var unSq = _undeterminedList[position];
                var possible = ComputePossible(i, toReeval.Number, innerSquare);
                if (possible.Count == 1)
                {
                    cantDecide = false;
                    var winner = possible[0];
                    _undeterminedList.Remove(position);
                    Squares[i][toReeval.Number] = winner;
                    Reevaluate(i, toReeval.Number, innerSquare, winner);
                }
                else
                {
                    if (possible.Count < fewest)
                        wild = unSq;
                    unSq.Possible = possible;
                }
            }
        }
    }

    private void ProcessRow(ref bool cantDecide, int fewest, ref UndeterminedSquare wild, ItemToReevaluate toReeval)
    {
        int queueRow = ("row" + toReeval.Number).GetHashCode();
        _queueDictionary[queueRow] = false;
        //iterate through all items in row
        for (int j = 0; j < _size; j++)
        {
            int position = toReeval.Number * _size + j;

            //it element is undetermined
            if (_undeterminedList.ContainsKey(position))
            {
                int innerSquare = GetInnerSquare(toReeval.Number, j);
                var unSq = _undeterminedList[position];
                //recompute possible
                var possible = ComputePossible(toReeval.Number, j, innerSquare);
                //success
                if (possible.Count == 1)
                {
                    cantDecide = false;
                    var winner = possible[0];
                    _undeterminedList.Remove(position);
                    Squares[toReeval.Number][j] = winner;
                    Reevaluate(toReeval.Number, j, innerSquare, winner);
                }
                else
                {
                    if (possible.Count < fewest)
                        wild = unSq;
                    unSq.Possible = possible;
                }
            }
        }
    }

    private List<int> ComputePossible(int row, int col, int innerSquare)
    {
      return _rows[row].RemainingValues.Intersect(_columns[col].RemainingValues)
        .Intersect(_innerSquares[innerSquare].RemainingValues).ToList();
    }

    private void FirstStep()
    {
      int fewest = _size;
      
      UndeterminedSquare wildElem = null;
      bool cantDecide = true;
      //start processing 1st wave
      for (int i = 0; i < 9; i++)
      {
        for (int j = 0; j < 9; j++)
        {
          if (Squares[i][j] == 0)
          {
            int innerSquare = GetInnerSquare(i, j);
            UndeterminedSquare unSq = new UndeterminedSquare()
            {
              Row = i,
              Column = j,
              Possible = ComputePossible(i, j, innerSquare)
            };
            //determine what could be
            if (unSq.Possible.Count == 1)
            {
              cantDecide = false;
              var winner = unSq.Possible[0];

              Squares[i][j] = winner;
              bool exists = false;

              //this will mark that some rows / columns / squares need reevaluating
              Reevaluate(i, j, innerSquare, winner);
            }
            else
            {
              if (unSq.Possible.Count < fewest)
                wildElem = unSq;
              _undeterminedList.Add(i * _size + j, unSq);
            }
          }
        }
      }
      if (cantDecide)
        CreateSavepoint(wildElem);
    }

    private int GetInnerSquare(int row, int column)
    {
      return 3 * (row / 3) + (column / 3);
    }

    private void Reevaluate(int i, int j, int innerSquare, int winner)
    {
      _rows[i].RemainingValues.Remove(winner);
      if (_rows[i].RemainingValues.Count == 0)
        i = -1;
      _columns[j].RemainingValues.Remove(winner);
      if (_columns[j].RemainingValues.Count == 0)
        j = -1;
      _innerSquares[innerSquare].RemainingValues.Remove(winner);
      if (_innerSquares[innerSquare].RemainingValues.Count == 0)
        innerSquare = -1;
      //place row, column & square into reeValuation Queue
      AddItemsInProcessQueue(i, j, innerSquare);
    }


    private void AddItemsInProcessQueue(int row, int column, int square)
    {
      int queueRow = ("row" + row).GetHashCode();
      int queueColumn = ("column" + column).GetHashCode();
      int queueSquare = ("square" + square).GetHashCode();

      //the row item is not in queue -> add it
      if(row!= -1 && (!_queueDictionary.ContainsKey(queueRow) || !_queueDictionary[queueRow]))
      {
        if (_queueDictionary.ContainsKey(queueRow))
          _queueDictionary[queueRow] = true;
        else
          _queueDictionary.Add(queueRow, true);
        _reEvaluationQueue.Enqueue(new ItemToReevaluate()
        {
          Type = ItemType.ROW,
          Number = row
        });
      }
      //the column item is not in queue -> add it
      if (column != -1 && (!_queueDictionary.ContainsKey(queueColumn) || !_queueDictionary[queueColumn]))
      {
        if (_queueDictionary.ContainsKey(queueColumn))
          _queueDictionary[queueColumn] = true;
        else
          _queueDictionary.Add(queueColumn, true);
        _reEvaluationQueue.Enqueue(new ItemToReevaluate()
        {
          Type = ItemType.COLUMN,
          Number = column
        });
      }

      if (square != -1 && (!_queueDictionary.ContainsKey(queueSquare) || !_queueDictionary[queueSquare]))
      {
        if (_queueDictionary.ContainsKey(queueSquare))
          _queueDictionary[queueSquare] = true;
        else
          _queueDictionary.Add(queueSquare, true);

        _reEvaluationQueue.Enqueue(new ItemToReevaluate()
        {
          Type = ItemType.SQUARE,
          Number = square
        });
      }

    }
  }



  public class UndeterminedSquare
  {
    public int Row { get; set; }
    public int Column { get; set; }
    public List<int> Possible { get; set; }
  }
  public class CollectionRemaining
  {
    public List<int> RemainingValues { get; set; }
  }
  public enum ItemType
  {
    ROW,COLUMN, SQUARE
  }
  public class ItemToReevaluate
  {
    public ItemType Type { get; set; }
    public int Number { get; set; }
  }
}
