var datatable;
var tasks;
var myChartV2;
const ctx = document.getElementById('myChart').getContext('2d');
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
    const location = $('#location :selected').value && $('#location :selected').value != 'no-location' ? $('#location :selected').value : 'ho-chi-minh';
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
    fetchChart();
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

const fetchChart = function() {
    const url = 'http://localhost:8087/api/chart-jobs';
    $.ajax(url, {
        success: (data) => {
            console.log(data)
            // console.log(data.java.length)
            // console.log(data.python.length)
            let arr = []
            let amounts = []
            for (var key in data) {
                console.log(data[key])
                console.log('key: ' + key + ', ' + key.length)
                arr.push(key)
                amounts.push(data[key].length)
            }
            myChartV2 = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: arr,
                    datasets: [{
                        label: '# of Votes',
                        data: amounts,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 159, 64, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
            });
            
        },
        error: (jqxhr, textStatus, error) => {
            console.log(jqxhr)
            console.log(error)
            console.log(textStatus)
        }
    });
}

$(document).ready(function () {
    fetchSpiders()
    fetchAlias()
    fetchChart()
});


// const myChart = new Chart(ctx, {
//     type: 'pie',
//     data: {
//         labels: ['dotnet', 'python', 'c', 'Java', 'golang', 'angular'],
//         datasets: [{
//             label: '# of Votes',
//             data: [1000, 2560, 3434, 3452, 4643, 1421],
//             backgroundColor: [
//                 'rgba(255, 99, 132, 0.2)',
//                 'rgba(54, 162, 235, 0.2)',
//                 'rgba(255, 206, 86, 0.2)',
//                 'rgba(75, 192, 192, 0.2)',
//                 'rgba(153, 102, 255, 0.2)',
//                 'rgba(255, 159, 64, 0.2)'
//             ],
//             borderColor: [
//                 'rgba(255, 99, 132, 1)',
//                 'rgba(54, 162, 235, 1)',
//                 'rgba(255, 206, 86, 1)',
//                 'rgba(75, 192, 192, 1)',
//                 'rgba(153, 102, 255, 1)',
//                 'rgba(255, 159, 64, 1)'
//             ],
//             borderWidth: 1
//         }]
//     },
// });