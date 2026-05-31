import java.util.*;

public class Solution {
    
    public int solution(int[] A) {
        if(A.length < 3){
            log("A.length < 3 -> returning 0");
            return 0;
        }

        List<Integer> peaks = getPeaks(A);
        log("peaks", peaks);
        
        // Fix: If there are absolutely no peaks, we must return 0 instantly.
        if(peaks.isEmpty()){
            return 0;
        }
        
        if(peaks.size() < 2){
            return peaks.size();
        }

        int[] nextPeaks = getNextPeaks(A, peaks);
        log("nextPeaks", nextPeaks);

        return getNumFlags(A, peaks, nextPeaks);
    }

    boolean isPeak(int[] A, int index){
        if(index == 0 || index >= A.length - 1){
            return false;
        }

        return A[index - 1] < A[index] && A[index] > A[index + 1];
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

    int[] getNextPeaks(int[] A, List<Integer> peaks){
        int[] nextPeaks = new int[A.length];

        int peakCntr = 0;
        int nextPeak = peaks.get(peakCntr);

        for(int i = 0; i < nextPeaks.length; i++){
            if(isPeak(A, i)){
                peakCntr++;
                if(peakCntr < peaks.size()){
                    nextPeak = peaks.get(peakCntr);
                } else {
                    nextPeak = -1;
                }
            }

            nextPeaks[i] = nextPeak;
        }

        return nextPeaks;
    }

    int getNumFlags(int[] A, List<Integer> peaks, int[] nextPeaks){
        int sqrRt = (int) Math.sqrt(A.length) + 1;

        log("sqrRt: " + sqrRt);

        for(int i = sqrRt; i > 0; i--){
            if(canPlace(A, peaks, nextPeaks, i)){
                log("can place: " + i);
                return i;
            }
        }

        return 0;
    }

    boolean canPlace(int[] A, List<Integer> peaks, int[] nextPeaks, int tryFlags){
        int flagCntr = 0;

        int flagIndex = nextPeaks[0];
        while(flagIndex >= 0){
            log("flagIndex: " + flagIndex + "; flagCntr: " + flagCntr);
            flagCntr++;
            if(flagCntr >= tryFlags){
                return true;
            }
            flagIndex = getNextFlagIndex(A, peaks, nextPeaks, tryFlags, flagIndex);
        }

        return flagCntr >= tryFlags;
    }

    int getNextFlagIndex(int[] A, List<Integer> peaks, int[] nextPeaks, int tryFlags, int flagIndex){
        int tryNextIndex = flagIndex + tryFlags;

        if(tryNextIndex >= A.length){
            return -1;
        }

        if(isPeak(A, tryNextIndex)){
            return tryNextIndex;
        }

        return nextPeaks[tryNextIndex];
    }

    void log(String str){
        System.out.println(str);
    }

    void log(String str, int[] A){
        log(str + ": " + toString(A));
    }

    void log(String str, List<Integer> A){
        log(str + ": " + toString(A));
    }

    String toString(int[] A){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < A.length; i++){
            if(i > 0){
                sb.append(", ");
            }
            sb.append(A[i]);
        }
        return sb.toString();
    }

    String toString(List<Integer> A){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < A.size(); i++){
            if(i > 0){
                sb.append(", ");
            }
            sb.append(A.get(i));
        }
        return sb.toString();
    }

    // =========================================================================
    // Test Suite Execution
    // =========================================================================
    public static void main(String[] args) {
        Solution solver = new Solution();

        System.out.println("--- Running Suite for Your Solution --- \n");

        // Test 1: Standard Codility Example Case
        int[] test1 = {1, 5, 3, 4, 3, 4, 1, 2, 3, 4, 6, 2};
        verify(1, solver.solution(test1), 3);

        // Test 2: Small array boundary (< 3 items)
        int[] test2 = {1, 5};
        verify(2, solver.solution(test2), 0);

        // Test 3: Tight crowded peaks (indices 1, 3, 5, 7)
        int[] test3 = {1, 4, 1, 4, 1, 4, 1, 4, 1};
        verify(3, solver.solution(test3), 2);

        // Test 4: Single peak scenario
        int[] test4 = {1, 10, 1};
        verify(4, solver.solution(test4), 1);
        
        // Test 5: Straight line (no peaks)
        int[] test5 = {1, 2, 3, 4, 5};
        // FIXED: Changed expected value to 0 since flags can only go on peaks.
        verify(5, solver.solution(test5), 0); 

        // Test 6: Breaking Case (Uneven distribution)
        // Peaks are at indices: 1, 7, 11.
        // If tryFlags = 3, we can place them at 1, then 1+3=4 -> nextPeak is 7, then 7+3=10 -> nextPeak is 11.
        // Total flags = 3. Your current nextPeaks lookup table setup causes this to return 2.
        int[] test6 = {1, 5, 1, 1, 1, 1, 1, 5, 1, 1, 1, 5, 1};
        verify(6, solver.solution(test6), 3);
    }

    private static void verify(int testNum, int actual, int expected) {
        if (actual == expected) {
            System.out.println("Test #" + testNum + ": PASSED (Result: " + actual + ")");
        } else {
            System.out.println("Test #" + testNum + ": FAILED (Expected: " + expected + ", Got: " + actual + ")");
        }
    }
}