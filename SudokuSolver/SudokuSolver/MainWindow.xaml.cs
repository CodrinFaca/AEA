using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace SudokuSolver
{
  /// <summary>
  /// Interaction logic for MainWindow.xaml
  /// </summary>
  public partial class MainWindow : Window
  {
    public SolveSudoku sudokuProblem = null;
    public int Size { get; set; }
    Stopwatch sw = new Stopwatch();
    public MainWindow()
    {
      InitializeComponent();
    }

    private void OpenFile(object sender, RoutedEventArgs e)
    {
      Stream myStream = null;
      OpenFileDialog theDialog = new OpenFileDialog();
      theDialog.Title = "Open Text File";
      theDialog.Filter = "TXT files|*.txt";
      theDialog.InitialDirectory = @"C:\";

      if (theDialog.ShowDialog() == true)
      {
        try
        {
          if ((myStream = theDialog.OpenFile()) != null)
          {
            using (myStream)
            {

              var sr = new StreamReader(myStream);
              var myStr = sr.ReadToEnd();
              Console.WriteLine(myStr);
              string[] lines = myStr.Split(new string[] { Environment.NewLine }, StringSplitOptions.None);
              Size = int.Parse(lines[0]);
              var inputarray = new int[Size][];
              for (var i = 1; i <= Size; i++)
              {
                Input.Text += lines[i] + Environment.NewLine;
                inputarray[i-1] = lines[i].Split(' ').Select(x => int.Parse(x)).ToArray();
              }
              sw.Start();
              sudokuProblem = new SolveSudoku(Size, inputarray);

            }
          }
        }
       
        catch (Exception ex)
        {
          MessageBox.Show("Error: Could not read file from disk. Original error: " + ex.Message);
        }

      }
      if (sudokuProblem != null)
      {
       var result = sudokuProblem.StartProcessing();
       sw.Stop();
       Statistics.Text = "Elapsed miliseconds: "+ sw.ElapsedMilliseconds.ToString();
       Output.Text = string.Empty;
       for (int i = 0; i < Size; i++)
       {
           for(int j = 0; j < Size; j++){
               Output.Text += result[i][j].ToString() + ' ';
           }
           Output.Text += Environment.NewLine;
       }
      }
    }
  }
}
