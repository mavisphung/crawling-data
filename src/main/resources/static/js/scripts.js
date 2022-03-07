var datatable;
const clickMe = function() {
    const keyword = document.getElementById('keyword').value;
    let url = 'http://localhost:8087/api/find-job-v3?keyword=' + keyword;
    alert(url);
    // $.get('http://localhost:8087/api/crawl/123job?l=Yên Bái', function(result) {
    //     console.log(result)
    // });

    if ($.fn.dataTable.isDataTable('#table_id')) {
        datatable.destroy();
    }
    else {
        datatable = $('#table_id').DataTable({
            ajax: {
                url: url, //'http://localhost:8087/api/crawl/123job?l=Yên Bái'
                dataSrc: 'data',
                cache: false
            },
            paging: false,
            searching: false,
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

    // datatable = $('#table_id').DataTable({
    //     ajax: {
    //         url: url, //'http://localhost:8087/api/crawl/123job?l=Yên Bái'
    //         dataSrc: 'data',
    //         cache: false
    //     },
    //     paging: false,
    //     searching: false,
    //     columns: [
    //         {
    //             data: 'title',
    //             width: '20%'
    //         },
    //         {
    //             data: 'company',
    //             width: '20%'
    //         },
    //         {
    //             data: 'location',
    //             width: '20%'
    //         },
    //         {
    //             data: 'salary',
    //             width: '20%'
    //         },
    //         {
    //             data: 'companyLogo',
    //             width: '20%'
    //         },
    //     ]
    // });
    
}

const fetchJobData = function() {
    const keyword = document.getElementById('keyword').value;
    let url = 'http://localhost:8087/api/find-job-v3?keyword=' + keyword;
    alert(url);
    $('#table_id').DataTable({
        ajax: {
            url: url,
            dataSrc: 'data'
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