var datatable;
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

const fetchJobData = function() {
    const keyword = document.getElementById('keyword').value;
    let url = 'http://localhost:8087/api/find-job-v3?keyword=' + keyword;
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
}

const exportExcel = function() {
}