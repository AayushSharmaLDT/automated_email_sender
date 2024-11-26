package com.email_sender.controller;

import com.email_sender.service.EmailSchedulerService;
import com.email_sender.util.CSVUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailSchedulerService emailSchedulerService;

    @PostMapping("/sendOrSchedule")
    public ResponseEntity<String> sendOrScheduleEmail(
            @RequestParam("subject") String subject,
            @RequestParam("from") String from,
            @RequestParam("content") String content,
            @RequestParam(value ="file", required = false) MultipartFile file,
            @RequestParam("isHtml") boolean isHtml,
            @RequestParam(value = "scheduleTime", required = false) String scheduleTime) {

        try {

            File csvFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(csvFile);


            List<String> emails = CSVUtil.parseEmails(csvFile.getAbsolutePath());

            if (scheduleTime == null || scheduleTime.isEmpty()) {

                emailSchedulerService.sendNow(subject, from, content, emails, isHtml);
                return ResponseEntity.ok("Emails sent successfully!");
            } else {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(scheduleTime, formatter);

                emailSchedulerService.scheduleEmail(subject, from, content, emails, isHtml, dateTime);
                return ResponseEntity.ok("Email scheduled successfully for " + scheduleTime);
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error: File processing failed!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}

