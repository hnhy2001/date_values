package com.example.date_values.service.impl;

import com.example.date_values.dto.DateValuesDto;
import com.example.date_values.entity.DateValues;
import com.example.date_values.entity.DateValuesHistory;
import com.example.date_values.entity.User;
import com.example.date_values.model.reponse.*;
import com.example.date_values.model.request.*;
import com.example.date_values.repository.BaseRepository;
import com.example.date_values.repository.DateValueHistoryRepository;
import com.example.date_values.repository.DateValuesRepository;
import com.example.date_values.service.DateValuesHistoryService;
import com.example.date_values.service.DateValuesService;
import com.example.date_values.service.UserService;
import com.example.date_values.util.DateUtil;
import com.example.date_values.util.MapperUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DateValuesServiceImpl extends BaseServiceImpl<DateValues> implements DateValuesService {

    @Autowired
    private DateValuesRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    DateValuesHistoryService service;

    @Override
    protected BaseRepository<DateValues> getRepository() {
        return repository;
    }

    @Override
    public BaseResponse getSpecialCycleStatistics(SpecialCycleStatisticsReq req) {
        if (req.getStartDate() < 20100101 || req.getEndDate() > DateUtil.getCurrenDate()) {
            return new BaseResponse().fail("Chỉ thống kê từ ngày 01/01/2010 đến nay!");
        }
        if (req.getData().isEmpty()) {
            return new BaseResponse().fail("Danh sách thống kê không được trống!");
        }
        return new BaseResponse().success(find(req));
    }

    public SpecialCycleStatisticsRes find(SpecialCycleStatisticsReq req) {
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0;date<=" + req.getEndDate() + ";date>=" + req.getStartDate())
                .page(0)
                .size(30000)
                .sort("date,asc")
                .build();

        List<DateValues> data = this.search(searchReq).getContent();
        List<DateValues> dateValuesList = new ArrayList<>();
        dateValuesList = data;
        Map<Long, Integer> dateValuesMap = new HashMap<>();

        int countGap = 1;
        int maxGap = 0;
        Long maxEndDate = 0L;
        int duringValue = 0;
        Long duringDate = 0L;
        int check = 0;
        for (DateValues element : dateValuesList) {
            dateValuesMap.put(element.getDate(), element.getValue() != null ? Integer.parseInt(element.getValue()) : 0);
            if (element.getValue() != null) {
                if (!req.getData().contains(
                        req.getHead() != 1 ?
                                Integer.parseInt(element.getValue().substring(element.getValue().length() - 2))
                                :Integer.parseInt(element.getValue().substring(0,2)))) {
                    countGap++;
                    if (countGap > maxGap) {
                        maxGap = countGap;
                        maxEndDate = element.getDate();
                    }
                } else {
                    countGap = 1;
                    duringDate = element.getDate();
                    duringValue = Integer.parseInt(element.getValue());
                    check = 1;
                }
            }
        }
        maxEndDate = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxEndDate : DateUtil.sum(maxEndDate, 1);
        maxGap = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxGap - 1 : maxGap;
        Long maxStartDate = DateUtil.subtract(maxEndDate, maxGap);

        SearchReq searchReqCheck = SearchReq.builder()
                .filter("id>0")
                .page(0)
                .size(30000)
                .sort("date,desc")
                .build();
        DateValues dateValues = new DateValues();
        List<DateValues> dateValuesCheck = this.search(searchReqCheck).getContent();
        if (req.getHead() == 1) {
            for (DateValues element : dateValuesCheck) {
                if (element.getValue() != null) {
                    if (req.getData().contains(Integer.parseInt(element.getValue().substring(0,2)))) {
                        dateValues = element;
                        break;
                    }
                }
            }
        } else {
            for (DateValues element : dateValuesCheck) {
                if (element.getValue() != null) {
                    if (req.getData().contains(Integer.parseInt(element.getValue().substring(element.getValue().length() - 2)))) {
                        dateValues = element;
                        break;
                    }
                }
            }
        }


        SpecialCycleStatisticsRes res;
        if (check == 1) {
            res = SpecialCycleStatisticsRes.builder()
                    .startDate(req.getStartDate())
                    .endDate(req.getEndDate())
                    .data(req.getData())
                    .maxStartDate(maxStartDate)
                    .maxEndDate(req.getData().contains(dateValuesMap.get(maxEndDate)) ? maxEndDate : 0)
                    .lastDate(dateValues.getDate())
                    .maxGap(maxGap)
                    .lastValue(Integer.parseInt(dateValues.getValue()))
                    .maxEndValue(req.getData().contains(dateValuesMap.get(maxEndDate)) ? dateValuesMap.get(maxEndDate) : 0)
                    .maxStartValue(dateValuesMap.get(maxStartDate))
                    .stubbornnessLevel(DateUtil.calculateDaysBetween(dateValues.getDate(), DateUtil.getCurrenDate()) - 1)
                    .duringDate(duringDate)
                    .duringValue(duringValue)
                    .check(check)
                    .build();
        } else {
            res = SpecialCycleStatisticsRes.builder()
                    .stubbornnessLevel(DateUtil.calculateDaysBetween(dateValues.getDate(), DateUtil.getCurrenDate()) - 1)
                    .lastDate(dateValues.getDate())
                    .lastValue(Integer.parseInt(dateValues.getValue()))
                    .check(check)
                    .build();
        }
        System.out.println(res);
        return res;
    }

    @Override
    public BaseResponse getTodayNumbersStatistics(TodayNumberStatisticsReq req) throws Exception {
//        if (req.getOperatorType() != 2 && req.getOperatorType() != 1){
//            return new BaseResponse().fail("OperatorType chỉ là 1 hoặc 2");
//        }
        if (req.getQuantity() != 10 && req.getQuantity() != 15 && req.getQuantity() != 20 && req.getQuantity() != 25 && req.getQuantity() != 30
                && req.getQuantity() != 35 && req.getQuantity() != 40 && req.getQuantity() != 45 && req.getQuantity() != 50 && req.getQuantity() != 55) {
            return new BaseResponse().fail("quantity chỉ là 10,15,20,25,30,35,40,45,50,55");
        }

        int check = 0;
        switch (req.getQuantity()) {
            case 10:
                check = 65;
                break;
            case 15:
                // code block to be executed if expression equals value2
                check = 50;
                break; // optional
            case 20:
                check = 48;
                break;
            case 25:
                check = 40;
                break;
            case 30:
                check = 35;
                break;
            case 35:
                check = 30;
                break;
            case 40:
                check = 25;
                break;
            case 45:
                check = 23;
                break;
            case 50:
                check = 22;
                break;
            case 55:
                check = 21;
                break;
        }

        List<Integer> checkList = new ArrayList<>();
        int merge = 0;
        do {
            SearchReq searchReq = SearchReq.builder()
                    .filter("id>0")
                    .page(0)
                    .size(check + 2)
                    .sort("date,desc")
                    .build();
            List<DateValues> tempDateValues = this.search(searchReq).getContent();
            List<DateValues> dateValues = new ArrayList<>(tempDateValues);
            DateValues dateValuesMerge = dateValues.remove(check);
            merge = Integer.parseInt(dateValuesMerge.getValue() != null ?dateValuesMerge.getValue().substring(dateValuesMerge.getValue().length() - 2): "00");
            dateValues.remove(dateValues.size() - 1);
            checkList = dateValues.stream().map(e -> Integer.parseInt(e.getValue() != null ? e.getValue().substring(e.getValue().length() - 2): "00" )).collect(Collectors.toList());
            check++;
        }while (checkList.contains(merge));

        for (int i = 0; i < 1000000; i++) {
            List<Integer> uniqueRandomNumbers = generateUniqueRandomNumbers(req.getQuantity() - 1, checkList);
            uniqueRandomNumbers.add(merge);
            SpecialCycleStatisticsRes result = find(SpecialCycleStatisticsReq.builder().startDate(20100101L).endDate(DateUtil.getCurrenDate()).data(uniqueRandomNumbers).head(0).build());
            if (result.getMaxGap() - result.getStubbornnessLevel() < 5 && result.getMaxGap() - result.getStubbornnessLevel()> 1){
                DateValuesHistory dateValuesHistory = DateValuesHistory.builder()
                        .startDate(result.getStartDate())
                        .endDate(result.getEndDate())
                        .maxStartDate(result.getMaxStartDate())
                        .maxEndDate(result.getMaxEndDate())
                        .lastDate(result.getLastDate())
                        .maxGap(result.getMaxGap())
                        .stubbornnessLevel(result.getStubbornnessLevel())
                        .data(result.getData().toString())
                        .date(DateUtil.getCurrenDate())
                        .status(0)
                        .quantity(result.getData().size())
                        .build();
                service.create(dateValuesHistory);
                return new BaseResponse().success(result);
            }

        }

        return new BaseResponse().fail("Vui lòng thử lại lần nữa");
    }

    @Override
    public BaseResponse crawlData(Long date) {
        String dateString = DateUtil.dateLongToString(date);
        if (repository.existsByDate(date)) {
            return new BaseResponse().fail(String.format("Kết quả ngày %s đã tồn tại", dateString));
        } else {
            try {
                // Kết nối đến trang web
                String url = String.format("https://xoso.com.vn/xsmb-%s.html", dateString); // Đổi thành URL của trang web bạn muốn crawl
                Document doc = Jsoup.connect(url).timeout(50000).userAgent("Mozilla").get();

                Element valueElement = doc.selectFirst("span#mb_prizeDB_item0");
                if (valueElement != null) {
                    String value = valueElement.text(); // Lấy nội dung text bên trong thẻ div
                    DateValues dateValues = DateValues.builder()
                            .date(date)
                            .value(value)
                            .build();
                    return new BaseResponse().success(this.create(dateValues));
                } else {
                    return new BaseResponse().fail("Lấy dữ liệu thất bại");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new BaseResponse().fail("Fail!");
        }

    }

    public void createHistory(DateValuesHistory dateValuesHistory) throws Exception {
        this.service.create(dateValuesHistory);
    }

    @Override
    public BaseResponse createPrimeNumbers(SpecialCycleStatisticsReq req) throws Exception {
        SpecialCycleStatisticsRes result = find(req);
        DateValuesHistory dateValuesHistory = DateValuesHistory.builder()
                .startDate(result.getStartDate())
                .endDate(result.getEndDate())
                .maxStartDate(result.getMaxStartDate())
                .maxEndDate(result.getMaxEndDate())
                .lastDate(result.getLastDate())
                .maxGap(result.getMaxGap())
                .stubbornnessLevel(result.getStubbornnessLevel())
                .data(result.getData().toString())
                .date(DateUtil.getCurrenDate())
                .status(1)
                .quantity(result.getData().size())
                .build();
        service.create(dateValuesHistory);
        return new BaseResponse().success(dateValuesHistory);
    }

    @Override
    public BaseResponse searchRangeNumbers(SpecialCycleStatisticsReq req) {
        if (req.getStartDate() < 20100101 || req.getEndDate() > DateUtil.getCurrenDate()) {
            return new BaseResponse().fail("Chỉ thống kê từ ngày 01/01/2010 đến nay!");
        }
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0;date<=" + req.getEndDate() + ";date>=" + req.getStartDate())
                .page(0)
                .size(30000)
                .sort("date,asc")
                .build();
        List<DateValues> data = this.search(searchReq).getContent();
        List<DateValues> dateValuesList = new ArrayList<>();
        if (req.getHead() != 1) {
            dateValuesList = data.stream()
                    .filter(e -> e.getValue() != null)
                    .map(e -> {
                        e.setValue(e.getValue().substring(e.getValue().length() - 2));
                        return e;
                    }).collect(Collectors.toList());
        } else {
            dateValuesList = data.stream()
                    .filter(e -> e.getValue() != null)
                    .map(e -> {
                        e.setValue(e.getValue().substring(0, 2));
                        return e;
                    }).collect(Collectors.toList());
        }
        if (req.getData().isEmpty()) {
            SearchRangeNumbersRes result = SearchRangeNumbersRes.builder()
                    .dateValues(MapperUtil.mapEntityListIntoDtoPage(dateValuesList, DateValuesDto.class))
//                    .numbers(req.getData())
//                    .dateList(itemList)
                    .build();
            return new BaseResponse().success(result);
        }
        SpecialCycleStatisticsRes findResult = this.find(req);
        int maxGapCheck = findResult.getMaxGap();
        if (req.getGap() > maxGapCheck) {
            return new BaseResponse().fail("Ngưỡng cực đại là" + maxGapCheck + "không thể nhập số lượng lớn hơn ngưỡng cực đại!");
        }
        List<SearchRangeNumberItemRes> itemList = new ArrayList<>();
        int countGap = 1;
        int maxGap = 0;
        Long maxEndDate;
        if (req.getGap() == 0) {
            for (DateValues element : dateValuesList) {
                if (element.getValue() != null) {
                    if (!req.getData().contains(Integer.parseInt(element.getValue()))) {
                        countGap++;
                        if (countGap >= maxGap) {
                            maxGap = countGap;
                            maxEndDate = element.getDate();
                            maxGap = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxGap - 1 : maxGap;
                            if (maxGap == maxGapCheck) {
                                Long maxEndDateItem = maxEndDate;
//                                if (findResult.getEndDate() != 0){
//                                    maxEndDateItem = DateUtil.subtract(maxEndDateItem, 1);
//                                }
                                SearchRangeNumberItemRes item = SearchRangeNumberItemRes.builder()
                                        .to(maxEndDateItem)
                                        .from(DateUtil.subtract(maxEndDateItem, maxGap - 1))
                                        .build();
                                itemList.add(item);
                            }
                        }
                    } else {
                        countGap = 1;
                    }
                }
            }
        } else {
            for (int i = 0; i < dateValuesList.size(); i++) {
                if (dateValuesList.get(i).getValue() != null) {
                    if (!req.getData().contains(Integer.parseInt(dateValuesList.get(i).getValue()))) {
                        countGap++;
                        if (countGap == req.getGap()) {
                            maxGap = countGap;
                            if (i == dateValuesList.size() - 1) {
                                maxEndDate = dateValuesList.get(i).getDate();
                                Long maxEndDateItem = maxEndDate;
                                SearchRangeNumberItemRes item = SearchRangeNumberItemRes.builder()
                                        .to(maxEndDateItem)
                                        .from(DateUtil.subtract(maxEndDateItem, countGap - 2))
                                        .build();
                                itemList.add(item);
                            } else {
                                if (req.getData().contains(Integer.parseInt(dateValuesList.get(i + 1).getValue()))) {
                                    maxEndDate = dateValuesList.get(i).getDate();
                                    maxGap = maxEndDate.equals(DateUtil.getCurrenDate()) ? maxGap - 1 : maxGap;
                                    Long maxEndDateItem = maxEndDate;
                                    SearchRangeNumberItemRes item = SearchRangeNumberItemRes.builder()
                                            .to(maxEndDateItem)
                                            .from(DateUtil.subtract(maxEndDateItem, countGap - 2))
                                            .build();
                                    itemList.add(item);
                                }
                            }
                        }
                    } else {
                        countGap = 1;
                    }
                }
            }
        }

        SearchRangeNumbersRes result = SearchRangeNumbersRes.builder()
                .dateValues(MapperUtil.mapEntityListIntoDtoPage(dateValuesList, DateValuesDto.class))
                .numbers(req.getData())
                .dateList(itemList)
                .maxGap(findResult.getMaxGap())
                .head(req.getHead())
                .build();

        return new BaseResponse().success(result);
    }

    @Override
    public BaseResponse crawlAllData() {
        List<String> dates = getDataDate();
        List<DateValues> result = new ArrayList<>();
        for (String date : dates) {
            DateValues item = new DateValues();
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
                Document doc = Jsoup.connect(url).timeout(50000).userAgent("Mozilla").get();
                String values = null;
                if (doc.selectFirst("span#mb_prizeDB_item0") != null) {
                    values = doc.selectFirst("span#mb_prizeDB_item0").text();
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

    @Override
    public BaseResponse statisticValuesOnWeek(StatisticValuesOnWeekReq req) {
        if (req.getStartDate() < 20100101 || req.getEndDate() > DateUtil.getCurrenDate()) {
            return new BaseResponse().fail("Chỉ thống kê từ ngày 01/01/2010 đến nay!");
        }
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0;date<=" + req.getEndDate() + ";date>=" + req.getStartDate())
                .page(0)
                .size(30000)
                .sort("date,asc")
                .build();
        List<DateValues> data = this.search(searchReq).getContent();
        if (data == null || data.isEmpty()) {
            // Xử lý khi dữ liệu trống
            StatisticValuesOnWeekRes emptyResult = new StatisticValuesOnWeekRes(
                    Collections.emptyList(),
                    Collections.emptyList()
            );
            return new BaseResponse().success(emptyResult);
        }

        // Sắp xếp dữ liệu theo ngày tăng dần
        List<DateValues> sortedData = data.stream()
                .sorted(Comparator.comparing(DateValues::getDate))
                .collect(Collectors.toList());

        // Lấy ngày đầu tiên trong dữ liệu
        LocalDate firstDate;
        try {
            firstDate = DateUtil.convertLongToLocalDate(sortedData.get(0).getDate());
        } catch (Exception e) {
            // Xử lý nếu ngày đầu tiên không hợp lệ
            System.err.println("Ngày đầu tiên không hợp lệ: " + sortedData.get(0));
            StatisticValuesOnWeekRes errorResult = new StatisticValuesOnWeekRes(
                    Collections.emptyList(),
                    Collections.emptyList()
            );
            return new BaseResponse().fail("Ngày đầu tiên không hợp lệ.");
        }

        // Nhóm dữ liệu theo tuần, bắt đầu từ tuần 1
        Map<Integer, List<DateValues>> groupedData = sortedData.stream()
                .collect(Collectors.groupingBy(
                        item -> {
                            try {
                                // Chuyển đổi ngày từ item
                                LocalDate currentDate = DateUtil.convertLongToLocalDate(item.getDate());

                                // Đưa firstDate và currentDate về ngày đầu tuần (Thứ Hai)
                                LocalDate firstMonday = firstDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                                LocalDate currentMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                                // Tính số tuần giữa firstMonday và currentMonday
                                long weeksBetween = ChronoUnit.WEEKS.between(firstMonday, currentMonday);
                                return (int) weeksBetween + 1; // Tuần bắt đầu từ 1
                            } catch (Exception e) {
                                // Xử lý ngoại lệ
                                System.err.println("Invalid date format for item: " + item);
                                return -1; // Xử lý riêng các item lỗi
                            }
                        }
                ));

        // Loại bỏ các nhóm có tuần = -1 (nếu có)
        groupedData.remove(-1);
        ModelMapper modelMapper = new ModelMapper();
        Map<Integer, List<DateValuesDto>> groupedDataDTO = new HashMap<>();
        for (Map.Entry<Integer, List<DateValues>> entry : groupedData.entrySet()) {
            List<DateValuesDto> dtoList = entry.getValue().stream()
                    .map(e -> modelMapper.map(e, DateValuesDto.class))
                    .collect(Collectors.toList());
            groupedDataDTO.put(entry.getKey(), dtoList);
        }

        // Tạo danh sách kết quả cho nhóm theo tuần
        List<StatisticValuesOnWeekItemRes> weeklyDataList = groupedDataDTO.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sắp xếp theo tuần tăng dần
                .map(entry -> new StatisticValuesOnWeekItemRes(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // Tạo danh sách các giá trị duy nhất với chỉ hai ký tự cuối cùng
//        List<String> uniqueValues = sortedData.stream()
//                .map(DateValues::getValue) // Trích xuất trường 'value'
//                .filter(Objects::nonNull) // Loại bỏ null để tránh lỗi
//                .map(value -> value.length() >= 2 ? value.substring(value.length() - 2) : value)
//                .distinct() // Loại bỏ các giá trị trùng lặp
//                .collect(Collectors.toList());
        List<String> uniqueValues = weeklyDataList.stream().map(e -> e.getItems().get(0).getValue())
                .filter(Objects::nonNull)
                .map(value -> value.length() >= 2 ? value.substring(value.length() - 2) : value)
                .distinct()
                .collect(Collectors.toList());

        // Tạo đối tượng Result
        StatisticValuesOnWeekRes result = new StatisticValuesOnWeekRes(weeklyDataList, uniqueValues);

        // Trả về phản hồi thành công
        return new BaseResponse().success(result);
    }

    @Override
    public BaseResponse statisticFrequency(StatisticFrequencyReq req) {
        if (req.getStartDate() < 20100101 || req.getEndDate() > DateUtil.getCurrenDate()) {
            return new BaseResponse().fail("Chỉ thống kê từ ngày 01/01/2010 đến nay!");
        }
        SearchReq searchReq = SearchReq.builder()
                .filter("id>0;date<=" + req.getEndDate() + ";date>=" + req.getStartDate())
                .page(0)
                .size(30000)
                .sort("date,asc")
                .build();
        List<DateValues> data = this.search(searchReq).getContent();
        // Sử dụng HashMap để đếm tần suất
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (DateValues item : data) {
            if (item.getValue() != null ){
                String value = req.getHead() == 1 ? item.getValue().substring(0,2): item.getValue().substring(item.getValue().length() - 2);
                frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
            }
            else {
                frequencyMap.put("Lễ - Tết", frequencyMap.getOrDefault("Lễ - Tết", 0) + 1);
            }
            // Tăng giá trị tần suất
        }


        return new BaseResponse().success(frequencyMap);
    }

    @Scheduled(cron = "0 00 19 * * ?")
    public BaseResponse ScheduledCrawl() {
        return this.crawlData(DateUtil.getCurrenDate());
    }

    public static List<Integer> generateUniqueRandomNumbers(int n, List<Integer> existingList) {
        Random random = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();

        // Add all numbers from the existing list to a set for easy lookup
        Set<Integer> existingSet = new HashSet<>(existingList);

        // Generate unique random numbers until we reach the desired count
        while (uniqueNumbers.size() < n) {
            int randomNumber = random.nextInt(100); // Random number between 0 and 99

            // Check if the random number is not in the existing list
            if (!existingSet.contains(randomNumber)) {
                uniqueNumbers.add(randomNumber);
            }
        }

        return new ArrayList<>(uniqueNumbers);
    }

    public List<String> getDataDate() {
        // Định dạng ngày
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Ngày bắt đầu và ngày kết thúc
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 02);

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
