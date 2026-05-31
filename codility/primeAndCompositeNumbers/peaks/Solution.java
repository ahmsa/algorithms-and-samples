/*
A non-empty array A consisting of N integers is given.

A peak is an array element which is larger than its neighbors. More precisely, it is an index P such that 0 < P < N − 1,  A[P − 1] < A[P] and A[P] > A[P + 1].

For example, the following array A:

    A[0] = 1
    A[1] = 2
    A[2] = 3
    A[3] = 4
    A[4] = 3
    A[5] = 4
    A[6] = 1
    A[7] = 2
    A[8] = 3
    A[9] = 4
    A[10] = 6
    A[11] = 2
has exactly three peaks: 3, 5, 10.

We want to divide this array into blocks containing the same number of elements. More precisely, we want to choose a number K that will yield the following blocks:

A[0], A[1], ..., A[K − 1],
A[K], A[K + 1], ..., A[2K − 1],
...
A[N − K], A[N − K + 1], ..., A[N − 1].
What's more, every block should contain at least one peak. Notice that extreme elements of the blocks (for example A[K − 1] or A[K]) can also be peaks, but only if they have both neighbors (including one in an adjacent blocks).

The goal is to find the maximum number of blocks into which the array A can be divided.

Array A can be divided into blocks as follows:

one block (1, 2, 3, 4, 3, 4, 1, 2, 3, 4, 6, 2). This block contains three peaks.
two blocks (1, 2, 3, 4, 3, 4) and (1, 2, 3, 4, 6, 2). Every block has a peak.
three blocks (1, 2, 3, 4), (3, 4, 1, 2), (3, 4, 6, 2). Every block has a peak. Notice in particular that the first block (1, 2, 3, 4) has a peak at A[3], because A[2] < A[3] > A[4], even though A[4] is in the adjacent block.
However, array A cannot be divided into four blocks, (1, 2, 3), (4, 3, 4), (1, 2, 3) and (4, 6, 2), because the (1, 2, 3) blocks do not contain a peak. Notice in particular that the (4, 3, 4) block contains two peaks: A[3] and A[5].

The maximum number of blocks that array A can be divided into is three.

Write a function:

class Solution { public int solution(int[] A); }

that, given a non-empty array A consisting of N integers, returns the maximum number of blocks into which A can be divided.

If A cannot be divided into some number of blocks, the function should return 0.

For example, given:

    A[0] = 1
    A[1] = 2
    A[2] = 3
    A[3] = 4
    A[4] = 3
    A[5] = 4
    A[6] = 1
    A[7] = 2
    A[8] = 3
    A[9] = 4
    A[10] = 6
    A[11] = 2
the function should return 3, as explained above.

Write an efficient algorithm for the following assumptions:

N is an integer within the range [1..100,000];
each element of array A is an integer within the range [0..1,000,000,000].
*/

// you can also use imports, for example:
import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

class Solution {
    public int solution(int[] A) {
        if(A.length < 3){
            return 0;
        }

        List<Integer> peaks = getPeaks(A);

        if(peaks.size() < 2){
            return peaks.size();
        }

        int[] peakCounter = new int[A.length];
        populatePeakCounter(A, peaks, peakCounter);
        return countPartitions(A, peaks, peakCounter);
    }

    List<Integer> getPeaks(int[] A){
        List<Integer> peaks = new ArrayList<Integer>();

        for(int i = 1; i < A.length - 1; i++){
            if(isPeak(A, i)){
                peaks.add(i);
            }
        }

        return peaks;
    }

    boolean isPeak(int[] A, int i){
        if(i >= A.length || i <= 0){
            return false;
        }

        return A[i - 1] < A[i] && A[i] > A[i + 1];
    }

    void populatePeakCounter(int[] A, List<Integer> peaks, int[] peakCounter){
        int peakCounterI = 0;
        for(int i = 0; i < peakCounter.length; i++){
            if(isPeak(A, i)){
                peakCounterI++;
            }
            peakCounter[i] = peakCounterI;
        }
    }

    int countPartitions(int[] A, List<Integer> peaks, int[] peakCounter){
        for(int elements = 2; elements < A.length; elements++){
            if(A.length % elements != 0){
                continue;
            }

            int parts = A.length / elements;

            if(canPartition(A, peaks, peakCounter, elements, parts)){
                return parts;
            }
        }

        return 1;
    }

    boolean canPartition(int[] A, List<Integer> peaks, int[] peakCounter, int elements, int parts){
        if(parts > peaks.size()){
            return false;
        }

        int from = 0;
        int to = elements - 1;
        boolean containsPeaks = isContainsPeaks(A, peakCounter, from, to);

        while(containsPeaks){
            from = to + 1;
            to = to + elements;

            if(to >= A.length){
                break;
            }

            containsPeaks = isContainsPeaks(A, peakCounter, from, to);
        }

        return containsPeaks;
    }

    boolean isContainsPeaks(int[] A, int[] peakCounter, int from, int to){
        int newFrom = from == 0? 0 : from - 1;

        int strtVal = peakCounter[newFrom];
        int endVal = peakCounter[to];

        return endVal > strtVal;
    }
}
