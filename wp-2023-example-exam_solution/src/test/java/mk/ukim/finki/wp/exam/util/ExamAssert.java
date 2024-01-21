package mk.ukim.finki.wp.exam.util;

public class ExamAssert {

    public static void assertEquals(String message, Object expected, Object actual) {
        if (expected == null && actual == null) {
            success(message, expected, actual);
        } else if (expected == null || actual == null) {
            fail(message, expected, actual);
        } else if (expected.equals(actual)) {
            success(message, expected, actual);
        } else {
            fail(message, expected, actual);
        }
    }

    private static void fail(String message, Object expected, Object actual) {
        SubmissionHelper.submitFailedAssert(message, expected, actual);
        throw new ExamAssertionException(message, expected, actual);
    }

    private static void success(String message, Object expected, Object actual) {
        SubmissionHelper.submitSuccessAssert(message, expected, actual);
    }


}
