package com.kreuterkeule.StudentSorter;

import com.kreuterkeule.StudentSorter.calculator.CalcRunner;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SortingTest {

    CalcRunner runner = new CalcRunner(new ArrayList<>() /* test data here List<UserEntity> */);

    @Test
    public void testCalculate() {
        runner.run();
    }

}
