var datatable;
var tasks;
const clickMe = function() {
    const keyword = document.getElementById('keyword').value;
    let url = 'http://localhost:8087/api/find-job-v3?keyword=' + keyword;
    // alert(url);
    // $.get('http://localhost:8087/api/crawl/123job?l=Yên Bái', function(result) {
    //     console.log(result)
    // });
}

const callAjax = () => {
    const keyword = document.getElementById('keyword').value;
    let url = 'http://localhost:8087/api/find-job-v3?keyword=' + keyword;

    $.ajax(url, {
        success: (data) => {
            console.log(data)
            if (data.length == 0) {
                alert('Fetch data failed')
            } else {
                alert('Fetch data successfully')
            }
        },
        error: (jqxhr, textStatus, error) => {
            console.log(error)
            console.log(textStatus)
        }
    });
}

const fetchSpiders = function() {
    console.log(`$.fn.dataTable.isDataTable('#tasks'): ${$.fn.dataTable.isDataTable('#tasks')}`)
    if ($.fn.dataTable.isDataTable('#tasks')) {
        tasks.destroy()
        tasks = $('#tasks').DataTable({
            ajax: {
                url: 'http://localhost:8087/api/get-spiders', //'http://localhost:8087/api/crawl/123job?l=Yên Bái'
                dataSrc: "data",
                cache: false
            },
            columns: [
                {
                    data: 'id',
                    width: '20%'
                },
                {
                    data: 'spiderName',
                    width: '20%'
                },
                {
                    data: 'keyword',
                    width: '20%'
                },
                {
                    data: 'location',
                    width: '20%'
                },
                {
                    data: 'createdAt',
                    width: '20%'
                },
            ]
        });
    }
    else {
        tasks = $('#tasks').DataTable({
            ajax: {
                url: 'http://localhost:8087/api/get-spiders', //'http://localhost:8087/api/crawl/123job?l=Yên Bái'
                dataSrc: "data",
                cache: false
            },
            retrieve: true,
            columns: [
                {
                    data: 'id',
                    width: '20%'
                },
                {
                    data: 'spiderName',
                    width: '20%'
                },
                {
                    data: 'keyword',
                    width: '20%'
                },
                {
                    data: 'location',
                    width: '20%'
                },
                {
                    data: 'createdAt',
                    width: '20%'
                },
            ]
        });
    }
}

const fetchJobData = function() {
    const keyword = document.getElementById('keyword').value;
    // const location = $('#location :selected').value || $('#location :selected').value == 'no-location' ? $('#location :selected').value : 'ho-chi-minh';
    const location = $('#location :selected').text() || $('#location :selected').value == 'no-location' ? $('#location :selected').text() : 'ho-chi-minh';
    let url = 'http://localhost:8087/api/find-job-v3?keyword=' + keyword + '&location=' + location;
    console.log("Url: " + url)
    console.log(`$.fn.dataTable.isDataTable('#table_id'): ${$.fn.dataTable.isDataTable('#table_id')}`)
    if ($.fn.dataTable.isDataTable('#table_id')) {
        datatable.destroy()
        datatable = $('#table_id').DataTable({
            ajax: {
                url: url, //'http://localhost:8087/api/crawl/123job?l=Yên Bái'
                dataSrc: 'data',
                cache: false
            },
            columns: [
                {
                    data: 'title',
                    width: '20%'
                },
                {
                    data: 'company',
                    width: '20%'
                },
                {
                    data: 'location',
                    width: '20%'
                },
                {
                    data: 'salary',
                    width: '20%'
                },
                {
                    data: 'companyLogo',
                    width: '20%'
                },
            ]
        });
    }
    else {
        datatable = $('#table_id').DataTable({
            ajax: {
                url: url, //'http://localhost:8087/api/crawl/123job?l=Yên Bái'
                dataSrc: 'data',
                cache: false
            },
            retrieve: true,
            columns: [
                {
                    data: 'title',
                    width: '20%'
                },
                {
                    data: 'company',
                    width: '20%'
                },
                {
                    data: 'location',
                    width: '20%'
                },
                {
                    data: 'salary',
                    width: '20%'
                },
                {
                    data: 'companyLogo',
                    width: '20%'
                },
            ]
        });
    }

    fetchSpiders();
}

const exportExcel = function() {
}

const fetchAlias = function() {
    const url = 'http://localhost:8087/api/add-data';
    $.ajax(url, {
        success: (data) => {
            alert('Fetch alias successfully')
        },
        error: (jqxhr, textStatus, error) => {
            console.log(error)
            console.log(textStatus)
        }
    });
}

$(document).ready(function () {
    fetchSpiders()
    fetchAlias()
});