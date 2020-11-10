package com.example.demo.exception.handler;

import com.example.demo.exception.NoTickerHistoryElementException;
import com.example.demo.exception.NotEnoughTickerHistoryException;
import com.example.demo.exception.ResponseErrorException;
import com.example.demo.exception.TickerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ExceptionErrorHandler {

    @ExceptionHandler(TickerNotFoundException.class)
    public ModelAndView handleTickerNotFoundException(TickerNotFoundException e) {
        log.error("Ticker Not Found Exception Err Handler");
        ModelAndView mav = new ModelAndView("/index");
        mav.addObject("errMessage", e);
        return mav;
    }

    @ExceptionHandler(NotEnoughTickerHistoryException.class)
    public ModelAndView handleNotEnoughTickerHistoryException(NotEnoughTickerHistoryException e) {
        log.error("Not Enough Ticker History Exception Err Handler");
        ModelAndView mav = new ModelAndView("/index");
        mav.addObject("errMessage", e);
        return mav;
    }

    @ExceptionHandler(ResponseErrorException.class)
    public ModelAndView handleResponseErrorException(ResponseErrorException e) {
        log.error("API Response Err Handler");
        ModelAndView mav = new ModelAndView("/index");
        mav.addObject("errMessage", e);
        return mav;
    }

    @ExceptionHandler(NoTickerHistoryElementException.class)
    public ModelAndView handleNoMaxProfitDataElementException(NoTickerHistoryElementException e) {
        log.error("No Max Profit Data Element Exception Err Handler");
        ModelAndView mav = new ModelAndView("/index");
        mav.addObject("errMessage", e);
        return mav;
    }
}
