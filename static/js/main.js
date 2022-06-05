let runButton = $('#run')
let runButtonSpinner = $('#runSpinner')
let submitButton = $('#submit')
let submitButtonSpinner = $('#submitSpinner')
let langSelector = $('#langSelector')
let input = $('#input')
let testResults = $('#testResults')
runButton.click(onRunClicked)
submitButton.click(onSubmitClicked)

function onRunClicked() {
    setLoading()
    $.ajax({
        type: 'POST',
        url: '/editor/run',
        data: JSON.stringify(new ProblemData()),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: true,
        success: (data, textStatus, jqXHR) => {
            removeLoading()
            testResults.html(GetResultBlock(data))
        },
        error: (jqXHR, textStatus, error) => {
            removeLoading()
            testResults.html(GetErrorBlock(jqXHR.responseText))
        }
    })
}

function onSubmitClicked() {
    setLoading()
    $.ajax({
        type: 'POST',
        url: '/editor/submit',
        data: JSON.stringify(new ProblemData()),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: true,
        success: (data, textStatus, jqXHR) => {
            removeLoading()
            let tests = WithIndex(data);
            testResults.html(GetTests(tests))
        },
        error: (jqXHR, textStatus, error) => {
            removeLoading()
            testResults.html(GetErrorBlock(jqXHR.responseText))
        }
    })
}

function setLoading() {
    runButton.prop('disabled', true)
    runButtonSpinner.removeClass('visually-hidden')
    submitButton.prop('disabled', true)
    submitButtonSpinner.removeClass('visually-hidden')
}

function removeLoading() {
    runButton.prop('disabled', false)
    runButtonSpinner.addClass('visually-hidden')
    submitButton.prop('disabled', false)
    submitButtonSpinner.addClass('visually-hidden')
}

function ProblemData() {
    this.code = window.editorInstance.getValue()
    this.language = langSelector.val()
    this.input = input.val()
    this.taskId = new URLSearchParams(window.location.search).get('id')
}

function GetErrorBlock(errorMessage) {
    errorMessage = errorMessage.replaceAll('\n', '<br>')
    return `<div class="text-danger">
                ${errorMessage}
            </div>`
}

function GetErrorTests(tests) {
    return tests.map((test) => GetErrorTest(test))
}

function GetErrorTest(test) {
    test.msg = test.msg.replaceAll('\n', '<br>')
    return `<div class="border border-danger text-success mt-1">
                <div class="bg-danger bg-opacity-25 text-center fs-5">Тест ${test.i}</div>
                <div class="text-wrap text-break m-2">${test.msg}</div>
            </div>`
}

function GetTests(tests) {
    return tests.map((test) => GetTest(test))
}

function GetTest(test) {
    test.msg = test.msg.replaceAll('\n', '<br>')
    if (!test.pass)
    return `<div class="border border-danger text-danger mt-1">
                <div class="bg-danger bg-opacity-25 text-center fs-5">Тест ${test.i}</div>
                <div class="text-wrap text-break m-2">${test.msg}</div>
            </div>`
    return `<div class="border border-success text-success mt-1">
                <div class="bg-success bg-opacity-25 text-center fs-5">Тест ${test.i}</div>
                <div class="text-wrap text-break m-2">${test.msg}</div>
            </div>`
}

function GetResultBlock(out, inp = input.val()) {
    inp = inp.replaceAll('\n', '<br>')
    out = out.replaceAll('\n', '<br>')
    return `<layout for="inputData">Входные данные:</layout>
            <div class="text-light input-data" id="inputData">
                <code>
                    ${inp}
                </code>
            </div>
            <layout for="outputData">Выходные данные:</layout>
            <div class="text-light output-data" id="outputData">
                <code>
                    ${out}
                </code>
            </div>`
}

function WithIndex(data) {
    return data.map((value, idx) => {
        value.i = idx + 1
        return value
    })
}