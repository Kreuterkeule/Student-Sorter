package com.kreuterkeule.StudentSorter;

import com.kreuterkeule.StudentSorter.calculator.CalcRunner;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SortingTest {

    CalcRunner runner = new CalcRunner(new ArrayList<>() /* test data here */);

    @Test
    public void testCalculate() {
        runner.run();
    }

}
