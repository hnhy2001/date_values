package com.example.date_values.service.impl;

import com.example.date_values.entity.DateMultiValues;
import com.example.date_values.model.reponse.BaseResponse;
import com.example.date_values.repository.BaseRepository;
import com.example.date_values.repository.DateMultiValuesRepository;
import com.example.date_values.service.DateMultiValuesService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class DateMultiValuesServiceImpl extends BaseServiceImpl<DateMultiValues> implements DateMultiValuesService {

    @Autowired
    DateMultiValuesRepository dateMultiValuesRepository;
    @Override
    protected BaseRepository<DateMultiValues> getRepository() {
        return dateMultiValuesRepository;
    }

    @Override
    public BaseResponse crawlAllData() {
        List<String> dates = getDataDate();
        List<DateMultiValues> result = new ArrayList<>();
        for (String date : dates) {
            DateMultiValues item = new DateMultiValues();
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Chuyển chuỗi ngày thành LocalDate
            LocalDate dateLocal = LocalDate.parse(date, inputFormatter);

            // Định dạng lại ngày thành YYYYMMDD
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formattedDate = dateLocal.format(outputFormatter);

            // Chuyển chuỗi YYYYMMDD thành Long
            Long longDate = Long.parseLong(formattedDate);
            item.setDate(longDate);

            try {
                // Kết nối đến trang web
                String url = String.format("https://xoso.com.vn/xsmb-%s.html", date); // Đổi thành URL của trang web bạn muốn crawl
                Document doc = Jsoup.connect(url).timeout(5000).userAgent("Mozilla").get();
                String values = null;
                if (doc.selectFirst("span#mb_prizeDB_item0") != null){
                    values = doc.selectFirst("span#mb_prizeDB_item0").text() + ","
                            + doc.selectFirst("span#mb_prize1_item0").text() + ","
                            + doc.selectFirst("span#mb_prize2_item0").text() + ","
                            + doc.selectFirst("span#mb_prize2_item1").text() + ","
                            + doc.selectFirst("span#mb_prize3_item0").text() + ","
                            + doc.selectFirst("span#mb_prize3_item1").text() + ","
                            + doc.selectFirst("span#mb_prize3_item2").text() + ","
                            + doc.selectFirst("span#mb_prize3_item3").text() + ","
                            + doc.selectFirst("span#mb_prize3_item4").text() + ","
                            + doc.selectFirst("span#mb_prize3_item5").text() + ","
                            + doc.selectFirst("span#mb_prize4_item0").text() + ","
                            + doc.selectFirst("span#mb_prize4_item1").text() + ","
                            + doc.selectFirst("span#mb_prize4_item2").text() + ","
                            + doc.selectFirst("span#mb_prize4_item3").text() + ","
                            + doc.selectFirst("span#mb_prize5_item0").text() + ","
                            + doc.selectFirst("span#mb_prize5_item1").text() + ","
                            + doc.selectFirst("span#mb_prize5_item2").text() + ","
                            + doc.selectFirst("span#mb_prize5_item3").text() + ","
                            + doc.selectFirst("span#mb_prize5_item4").text() + ","
                            + doc.selectFirst("span#mb_prize5_item5").text() + ","
                            + doc.selectFirst("span#mb_prize6_item0").text() + ","
                            + doc.selectFirst("span#mb_prize6_item1").text() + ","
                            + doc.selectFirst("span#mb_prize6_item2").text() + ","
                            + doc.selectFirst("span#mb_prize7_item0").text() + ","
                            + doc.selectFirst("span#mb_prize7_item1").text() + ","
                            + doc.selectFirst("span#mb_prize7_item2").text() + ","
                            + doc.selectFirst("span#mb_prize7_item3").text();
                }
                item.setValue(values);
                result.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.getRepository().saveAll(result);
        return new BaseResponse().success("check");
    }

    public List<String> getDataDate(){
        // Định dạng ngày
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Ngày bắt đầu và ngày kết thúc
        LocalDate startDate = LocalDate.of(2017, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // Tạo một danh sách để chứa các ngày
        List<String> dateList = new ArrayList<>();

        // Vòng lặp qua tất cả các ngày từ startDate đến endDate
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            // Thêm ngày hiện tại vào danh sách
            dateList.add(currentDate.format(formatter));
            // Tiến đến ngày tiếp theo
            currentDate = currentDate.plus(1, ChronoUnit.DAYS);
        }

        return dateList;
    }
}
