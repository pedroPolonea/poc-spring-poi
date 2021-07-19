package com.p.sx.service.impl;

import com.p.sx.model.OrderEntity;
import com.p.sx.model.OrderItemEntity;
import com.p.sx.service.OrderService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public OrderEntity importOrders(final MultipartFile file) throws IOException {
        return parseExcelFile(file);
    }

    private OrderEntity parseExcelFile(final MultipartFile file) throws IOException {
        final OrderEntity order = new OrderEntity();
        order.setFileName(file.getOriginalFilename());
        order.add(parseExcelFile(file.getInputStream()));

        return order;
    }

    private List parseExcelFile(final InputStream inputStream){

        try {
            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheet("Customers");

            sheet.iterator().forEachRemaining(cells -> cells.forEach(
                    cell -> System.out.println(cell)
            ));


            Iterator<Row> rows = sheet.iterator();

            List<OrderItemEntity> items = new ArrayList<OrderItemEntity>();

            AtomicInteger atomicInteger = new AtomicInteger(0);

            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if(atomicInteger.getAndIncrement() == 0) {
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                OrderItemEntity item = new OrderItemEntity();
                item.setLine(atomicInteger.intValue());

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    if(cellIndex==0) { // NAME
                        item.setName(currentCell.getStringCellValue());
                    } else if(cellIndex==1) { // HASH
                        item.setHash(currentCell.getStringCellValue());
                    } else if(cellIndex==2) { // AMOUNT
                        item.setAmount((int) currentCell.getNumericCellValue());
                    }

                    cellIndex++;
                }

                items.add(item);
            }

            // Close WorkBook
            workbook.close();

            return items;

        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }
}
