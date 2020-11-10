package com.example.demo.controller;

import com.example.demo.model.TickerHistory;
import com.example.demo.service.TickerHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {

    private final TickerHistoryService tickerHistoryService;

    @PostMapping("/postTickerData")
    public RedirectView postTickerData(@RequestParam String tickerSymbol) {
        log.info("postTickerData info message");
        tickerHistoryService.postTickerDataInfo(tickerSymbol.toUpperCase());
        return new RedirectView("/getTickerData/" + tickerSymbol.toUpperCase());
    }

    @GetMapping("getTickerData/{tickerSymbol}")
    public ModelAndView getTickerData(@PathVariable String tickerSymbol) {
        log.info("getTickerData info message");
        final ModelAndView mav = new ModelAndView("index");
        final TickerHistory tickerHistory = tickerHistoryService.getTickerDataInfo(tickerSymbol);
        mav.addObject("tickerCostInfo", tickerHistory);
        return mav;
    }
}
