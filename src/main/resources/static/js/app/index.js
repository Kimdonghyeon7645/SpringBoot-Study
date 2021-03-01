/**
 * 브라우저의 스코프는 공용 공간으로 쓰임 -> 함수 이름이 중복되면 나중에 로딩된 js의 함수가 먼저 로딩된 js의 함수를 덮어씀(위험 존재)
 * 이를 피하기위해 : index.js 만의 유효범위(=main라는 객체)를 만들어 사용(=해당 객체, 유효범위 안에 필요한 모든 함수를 선언)하는 방법
 *
 * @type {{init: main.init, save: main.save}}
 */
var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });

        $('#btn-update').on('click', function () {
            _this.update();
        });

        // $('#btn-delete').on('click', function () {
        //     _this.delete();
        // });
    },

    save : function () {
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = '/';     // 글 등록 성공시 -> '/'(=메인 페이지)로 이동
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    update : function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    // delete : function () {
    //     var id = $('#id').val();
    //
    //     $.ajax({
    //         type: 'DELETE',
    //         url: '/api/v1/posts/'+id,
    //         dataType: 'json',
    //         contentType:'application/json; charset=utf-8'
    //     }).done(function() {
    //         alert('글이 삭제되었습니다.');
    //         window.location.href = '/';
    //     }).fail(function (error) {
    //         alert(JSON.stringify(error));
    //     });
    // }

};

main.init();
