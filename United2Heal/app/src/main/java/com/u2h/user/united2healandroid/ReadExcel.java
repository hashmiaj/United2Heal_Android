package com.u2h.user.united2healandroid;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReadExcel extends Fragment {
    ArrayList<ExcelDataPoint> dataArray;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataArray=new ArrayList<>();

        return inflater.inflate(R.layout.read_excel,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView spreadsheetListView= (ListView) view.findViewById(R.id.spreadsheetDataListView);
        Button readSpreadsheetButton= (Button) view.findViewById(R.id.readSpreadsheetButton);
        readSpreadsheetButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                readExcel();
                SpreadsheetListAdapter listAdapter=new SpreadsheetListAdapter(getContext(),dataArray);
                spreadsheetListView.setAdapter(listAdapter);
            }
        });

    }

    private void readExcel() {
        AssetManager assets=getActivity().getAssets();
        try {
            InputStream excelFile = assets.open("testfile.xlsx");
            XSSFWorkbook workbook=new XSSFWorkbook(excelFile);
            XSSFSheet sheet=workbook.getSheetAt(0);
            int rowCount=sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator=workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb= new StringBuilder();
            for(int r=0; r<rowCount;r++)
            {
                Row row=sheet.getRow(r);
                int colCount=row.getPhysicalNumberOfCells();
                for(int c=0; c<colCount;c++)
                {
                String value=getCellAsString(row,c,formulaEvaluator);
                sb.append(value + ",");
                }
                sb.append(":");
            }
            fillDataArray(sb);

        }
        catch (FileNotFoundException e)
        {
            Log.e("FileNotFoundException",e.getMessage());
        }
        catch (IOException e)
        {
            Log.e("IOException error",e.getMessage());
        }


    }
    //Return excel value as apprpriate string
    private String getCellAsString(Row row, int c, FormulaEvaluator formularEvaluator)
    {
        String value="";
        try{
            Cell cell= row.getCell(c);
            CellValue cellValue=formularEvaluator.evaluate(cell);
            switch(cellValue.getCellType())
            {
                case Cell.CELL_TYPE_BOOLEAN:
                    value=""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue=cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell))
                    {
                        double date= cellValue.getNumberValue();
                        SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/YY");
                        value=formatter.format(HSSFDateUtil.getJavaDate(date));
                    }
                    else
                    {
                        value=""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value=""+cellValue.getStringValue();
                    break;
            }
        }
        catch (NullPointerException e)
        {
            Log.e("NullPointerException",e.getMessage());
        }
    return value;
    }
    /*
    Parse the previously created string builder with
    rows seperated by ":" and columns by ",". Fill the data
    array with the parsed values.
    */
    private void fillDataArray(StringBuilder mStringBuilder)
    {
        String[] rows=mStringBuilder.toString().split(":");
        for(int r=0; r<rows.length;r++)
        {
            String[] col=rows[r].split(",");
            double quantity= Double.parseDouble(col[1]);
            dataArray.add(new ExcelDataPoint(col[0],quantity));
        }

    }

}
