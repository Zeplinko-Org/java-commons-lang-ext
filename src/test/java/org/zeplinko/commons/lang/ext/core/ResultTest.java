package org.zeplinko.commons.lang.ext.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResultTest {

    @Test
    void testOkResultShouldBeSuccess() {
        Result<Integer, String> result = Result.ok(10);
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertFalse(result.isFailure());
        Assertions.assertEquals(10, result.getData());
    }

    @Test
    void testErrorResultShouldBeFailure() {
        Result<Integer, String> result = Result.error("Error occurred");
        Assertions.assertTrue(result.isFailure());
        Assertions.assertFalse(result.isSuccess());
        Assertions.assertEquals("Error occurred", result.getError());
    }

    @Test
    void testOrElseWithSuccessResult() {
        Result<Integer, String> result = Result.ok(10);
        int value = result.orElse(5);
        Assertions.assertEquals(10, value);
    }

    @Test
    void testOrElseWithFailureResult() {
        Result<Integer, String> result = Result.error("Error occurred");
        int value = result.orElse(5);
        Assertions.assertEquals(5, value);
    }

    @Test
    void test_whenOrElseGetSupplierCalledOnSuccessResult_thenSupplierIsNotCalled() {
        Result<Integer, String> result = Result.ok(10);
        int value = result.orElseGet(() -> 5);
        Assertions.assertEquals(10, value);
    }

    @Test
    void test_whenOrElseGetSupplierCalledOnFailureResult_thenSupplierIsNotCalled() {
        Result<Integer, String> result = Result.error("Error occurred");
        int value = result.orElseGet(() -> 5);
        Assertions.assertEquals(5, value);
    }

    @Test
    void testOrElseGetWithSuccessResult() {
        Result<Integer, String> result = Result.ok(10);
        int value = result.orElseGet(error -> 5);
        Assertions.assertEquals(10, value);
    }

    @Test
    void testOrElseGetWithFailureResult() {
        Result<Integer, String> result = Result.error("Error occurred");
        int value = result.orElseGet(error -> 5);
        Assertions.assertEquals(5, value);
    }

    @Test
    void testOrElseThrowWithSuccessResult() throws Exception {
        Result<Integer, String> result = Result.ok(10);
        int value = result.orElseThrow(Exception::new);
        Assertions.assertEquals(10, value);
    }

    @Test
    void testOrElseThrowWithFailureResult() {
        Result<Integer, String> result = Result.error("Error occurred");
        Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> result.orElseThrow(Exception::new)
        );
        Assertions.assertEquals("Error occurred", exception.getMessage());
    }

    @Test
    void test_whenSuccessResultIsMappedToValue_thenMappedResultIsReturned() {
        Result<Integer, String> result = Result.ok(10);
        Result<Float, String> newResult = result.map(5.0f);

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(5.0f, newResult.getData());
    }

    @Test
    void test_whenFailureResultIsMappedToValue_thenMappingIsSkippedAndFailedResultIsReturned() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Float, String> newResult = result.map(5.0f);

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals("Error occurred", newResult.getError());
    }

    @Test
    void test_whenSuccessResultIsMapped_thenMappedResultIsReturned() {
        Result<Integer, String> result = Result.ok(10);
        Result<Float, String> newResult = result.map(it -> (float) it * 2);

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(20.0f, newResult.getData());
    }

    @Test
    void test_whenFailureResultIsMapped_thenMappingIsSkippedAndFailedResultIsReturned() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Float, String> newResult = result.map(it -> (float) it * 2);

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals("Error occurred", newResult.getError());
    }

    @Test
    void test_whenOtherwiseValueCalledOnSuccessResult_thenOriginalValueIsReturned() {
        Result<Integer, String> result = Result.ok(10);
        Result<Integer, String> newResult = result.otherwise(20);

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(10, newResult.getData());
    }

    @Test
    void test_whenOtherwiseValueCalledOnFailureResult_thenProvidedValueIsReturned() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Integer, String> newResult = result.otherwise(20);

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(20, newResult.getData());
    }

    @Test
    void test_whenOtherwiseCalledOnSuccessResult_thenOriginalValueIsReturned() {
        Result<Integer, String> result = Result.ok(10);
        Result<Integer, String> newResult = result.otherwise(str -> 20);

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(10, newResult.getData());
    }

    @Test
    void test_whenOtherwiseCalledOnFailureResult_thenProvidedValueIsReturned() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Integer, String> newResult = result.otherwise(str -> 20);

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(20, newResult.getData());
    }

    @Test
    void test_givenSuccessResultMapper_whenComposeCalledOnSuccessResult_thenSuccessMapperIsCalled() {
        Result<Integer, String> result = Result.ok(10);
        Result<Float, Double> newResult = result.compose(num -> Result.ok(20.f), str -> Result.error(50d));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(20.0f, newResult.getData());
    }

    @Test
    void test_givenFailureResultMapper_whenComposeCalledOnSuccessResult_thenSuccessMapperIsCalled() {
        Result<Integer, String> result = Result.ok(10);
        Result<Float, Double> newResult = result.compose(num -> Result.error(30d), str -> Result.ok(20.f));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals(30d, newResult.getError());
    }

    @Test
    void test_givenSuccessResultMapper_whenComposeCalledOnFailureResult_thenFailureMapperIsCalled() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Float, Double> newResult = result.compose(num -> Result.error(50d), str -> Result.ok(20.f));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(20.0f, newResult.getData());
    }

    @Test
    void test_givenFailureResultMapper_whenComposeCalledOnFailureResult_thenFailureMapperIsCalled() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Float, Double> newResult = result.compose(num -> Result.ok(20.f), str -> Result.error(30d));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals(30d, newResult.getError());
    }

    @Test
    void test_givenSuccessMapper_whenFlatMapCalledOnSuccessResult_thenMapperIsInvoked() {
        Result<Integer, String> result = Result.ok(10);
        Result<Float, String> newResult = result.flatMap(num -> Result.ok(20.0f));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(20.0f, newResult.getData());
    }

    @Test
    void test_givenFailureMapper_whenFlatMapCalledOnSuccessResult_thenMapperIsInvoked() {
        Result<Integer, String> result = Result.ok(10);
        Result<Float, String> newResult = result.flatMap(num -> Result.error("Error occurred"));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals("Error occurred", newResult.getError());
    }

    @Test
    void test_givenSuccessMapper_whenFlatMapCalledOnFailureResult_thenMapperIsSkipped() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Float, String> newResult = result.flatMap(num -> Result.ok(20.0f));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals("Error occurred", newResult.getError());
    }

    @Test
    void test_givenFailureMapper_whenFlatMapCalledOnFailureResult_thenMapperIsSkipped() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Float, String> newResult = result.flatMap(num -> Result.error("Error Occurred Again"));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals("Error occurred", newResult.getError());
    }

    @Test
    void test_givenSuccessMapper_whenRecoverCalledOnSuccessResult_thenMapperIsSkipped() {
        Result<Integer, String> result = Result.ok(10);
        Result<Integer, Double> newResult = result.recover(str -> Result.ok(20));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(10, newResult.getData());
    }

    @Test
    void test_givenFailureMapper_whenRecoverCalledOnSuccessResult_thenMapperIsSkipped() {
        Result<Integer, String> result = Result.ok(10);
        Result<Integer, Double> newResult = result.recover(str -> Result.error(30d));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(10, newResult.getData());
    }

    @Test
    void test_givenSuccessMapper_whenRecoverCalledOnFailureResult_thenMapperIsInvoked() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Integer, Double> newResult = result.recover(str -> Result.ok(20));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(20, newResult.getData());
    }

    @Test
    void test_givenFailureMapper_whenRecoverCalledOnFailureResult_thenMapperIsInvoked() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Integer, Double> newResult = result.recover(str -> Result.error(50d));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals(50d, newResult.getError());
    }

    @Test
    void test_givenSuccessTransformer_whenTransformIsCalledOnSuccessResult_thenTransformedResultIsReturned() {
        Result<Integer, String> result = Result.ok(10);
        Result<Float, Double> newResult = result.transform(r -> Result.ok(20.f));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(20.0f, newResult.getData());
    }

    @Test
    void test_givenSuccessTransformer_whenTransformIsCalledOnFailureResult_thenTransformedResultIsReturned() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Float, Double> newResult = result.transform(r -> Result.ok(20.f));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isSuccess());
        Assertions.assertEquals(20.0f, newResult.getData());
    }

    @Test
    void test_givenFailureTransformer_whenTransformIsCalledOnSuccessResult_thenTransformedResultIsReturned() {
        Result<Integer, String> result = Result.ok(10);
        Result<Float, Double> newResult = result.transform(r -> Result.error(50d));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals(50d, newResult.getError());
    }

    @Test
    void test_givenFailureTransformer_whenTransformIsCalledOnFailureResult_thenTransformedResultIsReturned() {
        Result<Integer, String> result = Result.error("Error occurred");
        Result<Float, Double> newResult = result.transform(r -> Result.error(50d));

        Assertions.assertNotNull(newResult);
        Assertions.assertTrue(newResult.isFailure());
        Assertions.assertEquals(50d, newResult.getError());
    }

    @Test
    void test_whenHandleCalledOnSuccessResult_thenSuccessHandlerIsInvoked() {
        boolean[] successHandlerCalled = { false };
        boolean[] failureHandlerCalled = { false };
        Result<Integer, String> result = Result.ok(10);

        Result<Integer, String> returnedResult = result
                .handle(num -> successHandlerCalled[0] = true, str -> failureHandlerCalled[0] = true);

        Assertions.assertNotNull(returnedResult);
        Assertions.assertSame(result, returnedResult);
        Assertions.assertTrue(successHandlerCalled[0]);
        Assertions.assertFalse(failureHandlerCalled[0]);
    }

    @Test
    void test_whenHandleCalledOnFailedResult_thenFailureHandlerIsInvoked() {
        boolean[] successHandlerCalled = { false };
        boolean[] failureHandlerCalled = { false };
        Result<Integer, String> result = Result.error("Error occurred");

        Result<Integer, String> returnedResult = result
                .handle(num -> successHandlerCalled[0] = true, str -> failureHandlerCalled[0] = true);

        Assertions.assertNotNull(returnedResult);
        Assertions.assertSame(result, returnedResult);
        Assertions.assertFalse(successHandlerCalled[0]);
        Assertions.assertTrue(failureHandlerCalled[0]);
    }

    @Test
    void test_whenHandleResultCalledOnSuccessResult_thenHandlerIsInvoked() {
        boolean[] handlerCalled = { false };
        Result<Integer, String> result = Result.ok(10);

        Result<Integer, String> returnedResult = result.handleResult(r -> handlerCalled[0] = true);

        Assertions.assertNotNull(returnedResult);
        Assertions.assertSame(result, returnedResult);
        Assertions.assertTrue(handlerCalled[0]);
    }

    @Test
    void test_whenHandleResultCalledOnFailedResult_thenHandlerIsInvoked() {
        boolean[] handlerCalled = { false };
        Result<Integer, String> result = Result.error("Error occurred");

        Result<Integer, String> returnedResult = result.handleResult(r -> handlerCalled[0] = true);

        Assertions.assertNotNull(returnedResult);
        Assertions.assertSame(result, returnedResult);
        Assertions.assertTrue(handlerCalled[0]);
    }

    @Test
    void test_whenHandleSuccessCalledOnSuccessResult_thenHandlerIsInvoked() {
        boolean[] handlerCalled = { false };
        Result<Integer, String> result = Result.ok(10);

        Result<Integer, String> returnedResult = result.handleSuccess(r -> handlerCalled[0] = true);

        Assertions.assertNotNull(returnedResult);
        Assertions.assertSame(result, returnedResult);
        Assertions.assertTrue(handlerCalled[0]);
    }

    @Test
    void test_whenHandleSuccessCalledOnFailedResult_thenHandlerIsSkipped() {
        boolean[] handlerCalled = { false };
        Result<Integer, String> result = Result.error("Error occurred");

        Result<Integer, String> returnedResult = result.handleSuccess(r -> handlerCalled[0] = true);

        Assertions.assertNotNull(returnedResult);
        Assertions.assertSame(result, returnedResult);
        Assertions.assertFalse(handlerCalled[0]);
    }

    @Test
    void test_whenHandleFailureCalledOnSuccessResult_thenHandlerIsSkipped() {
        boolean[] handlerCalled = { false };
        Result<Integer, String> result = Result.ok(10);

        Result<Integer, String> returnedResult = result.handleFailure(r -> handlerCalled[0] = true);

        Assertions.assertNotNull(returnedResult);
        Assertions.assertSame(result, returnedResult);
        Assertions.assertFalse(handlerCalled[0]);
    }

    @Test
    void test_whenHandleFailureCalledOnFailedResult_thenHandlerIsInvoked() {
        boolean[] handlerCalled = { false };
        Result<Integer, String> result = Result.error("Error occurred");

        Result<Integer, String> returnedResult = result.handleFailure(r -> handlerCalled[0] = true);

        Assertions.assertNotNull(returnedResult);
        Assertions.assertSame(result, returnedResult);
        Assertions.assertTrue(handlerCalled[0]);
    }
}
