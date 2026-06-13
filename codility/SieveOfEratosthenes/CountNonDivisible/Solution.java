/*
You are given an array A consisting of N integers.

For each number A[i] such that 0 ≤ i < N, we want to count the number of elements of the array that are not the divisors of A[i]. We say that these elements are non-divisors.

For example, consider integer N = 5 and array A such that:

    A[0] = 3
    A[1] = 1
    A[2] = 2
    A[3] = 3
    A[4] = 6
For the following elements:

A[0] = 3, the non-divisors are: 2, 6,
A[1] = 1, the non-divisors are: 3, 2, 3, 6,
A[2] = 2, the non-divisors are: 3, 3, 6,
A[3] = 3, the non-divisors are: 2, 6,
A[4] = 6, there aren't any non-divisors.
Write a function:

class Solution { public int[] solution(int[] A); }

that, given an array A consisting of N integers, returns a sequence of integers representing the amount of non-divisors.

Result array should be returned as an array of integers.

For example, given:

    A[0] = 3
    A[1] = 1
    A[2] = 2
    A[3] = 3
    A[4] = 6
the function should return [2, 4, 3, 2, 0], as explained above.

Write an efficient algorithm for the following assumptions:

N is an integer within the range [1..50,000];
each element of array A is an integer within the range [1..2 * N].
*/

import java.util.*;

public class Solution {
    
    public int[] solution(int[] A) {
        if (A == null || A.length == 0) {
            return new int[0];
        }
        int[] inputCounts = getInputCounts(A);
        return getNonDivisors(A, inputCounts);
    }

    private int[] getInputCounts(int[] A) {
        int[] inputCounts = new int[100001];

        for (int i = 0; i < A.length; i++) {
            int x = A[i];
            inputCounts[x] = inputCounts[x] + 1;
        }

        return inputCounts;
    }

    private int[] getNonDivisors(int[] A, int[] inputCounts) {
        int[] nonDivisors = new int[A.length];

        int[] solutions = new int[inputCounts.length];
        boolean[] solved = new boolean[inputCounts.length];

        for (int i = 0; i < A.length; i++) {
            nonDivisors[i] = getElementNonDivisors(A, inputCounts, solutions, solved, i);
        }

        return nonDivisors;
    }

    private int getElementNonDivisors(int[] A, int[] inputCounts, int[] solutions, boolean[] solved, int index) {
        int element = A[index];

        if (solved[element]) {
            return solutions[element];
        }

        int countDivisors = inputCounts[1];

        if (element > 1) {
            countDivisors += inputCounts[element];
        }

        int sqrRt = (int) Math.sqrt(element);

        for (int div = sqrRt; div > 1; div--) {
            if (element % div == 0) {
                countDivisors += inputCounts[div];

                int counterPart = element / div;

                if (counterPart != div) {
                    countDivisors += inputCounts[counterPart];
                }
            }
        }

        int countNonDivisors = A.length - countDivisors;
        solved[element] = true;
        solutions[element] = countNonDivisors;

        return countNonDivisors;
    }

    // =========================================================================
    // TEST SUITE
    // =========================================================================
    public static void main(String[] args) {
        Solution solver = new Solution();

        System.out.println("Running Test Suite...\n");

        // Test Case 1: Example from the problem description
        runTest(solver, "TC1: Problem Example", 
                new int[]{3, 1, 2, 3, 6}, 
                new int[]{2, 4, 3, 2, 0});

        // Test Case 2: Minimal array with identical elements
        runTest(solver, "TC2: Identical Elements", 
                new int[]{4, 4, 4, 4}, 
                new int[]{0, 0, 0, 0});

        // Test Case 3: Element containing 1s and primes
        runTest(solver, "TC3: Primes and Ones", 
                new int[]{1, 2, 3, 5, 7}, 
                new int[]{4, 3, 3, 3, 3});

        // Test Case 4: Perfect Squares
        runTest(solver, "TC4: Perfect Squares Boundary Check", 
                new int[]{1, 4, 16}, 
                new int[]{2, 1, 0});

        // Test Case 5: Single Element array
        runTest(solver, "TC5: Single Element (N=1 Boundary)", 
                new int[]{42}, 
                new int[]{0});

        // Test Case 6: Elements that don't divide each other at all
        runTest(solver, "TC6: Mutually Exclusive Composites", 
                new int[]{8, 9, 25}, 
                new int[]{2, 2, 2});
    }

    private static void runTest(Solution solver, String testName, int[] input, int[] expected) {
        int[] result = solver.solution(input);
        boolean passed = Arrays.equals(result, expected);
        
        System.out.println("--- " + testName + " ---");
        System.out.println("Input   : " + Arrays.toString(input));
        System.out.println("Expected: " + Arrays.toString(expected));
        System.out.println("Result  : " + Arrays.toString(result));
        System.out.println("Status  : " + (passed ? "PASSED ✅" : "FAILED ❌") + "\n");
    }
}