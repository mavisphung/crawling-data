
const clickMe = function() {

    // $.get('http://localhost:8087/api/crawl/123job?l=Yên Bái', function(result) {
    //     console.log(result)
    // });

    $('#table_id').DataTable({
        ajax: {
            url: 'http://localhost:8087/api/crawl/123job?l=Yên Bái', 
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

// $(document).ready(function () {
//     $('#table_id').DataTable();
// });
