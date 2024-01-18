merge into authors as dst
using
(
    select 'Author_1' as full_name union all
    select 'Author_2' as full_name union all
    select 'Author_3' as full_name
) src ON src.full_name = dst.full_name
when not matched then
    insert (full_name)
    values (src.full_name)
when matched then
    update
    set full_name = src.full_name;

merge into genres as dst
    using
        (
            select 'Genre_1' as name union all
            select 'Genre_2' as name union all
            select 'Genre_3' as name union all
            select 'Genre_4' as name union all
            select 'Genre_5' as name union all
            select 'Genre_6' as name
        ) src ON src.name = dst.name
when not matched then
    insert (name)
    values (src.name)
when matched then
    update
    set name = src.name;

merge into books as dst
    using
        (
            select 'BookTitle_1' as title, 1 as author_id union all
            select 'BookTitle_2' as title, 2 as author_id union all
            select 'BookTitle_3' as title, 3 as author_id
        ) src ON src.title = dst.title
when not matched then
    insert (title, author_id)
    values (src.title, src.author_id)
when matched then
    update
    set title = src.title, author_id = src.author_id;


merge into books_genres as dst
    using
        (
            select 1 as book_id, 1 as genre_id union all
            select 1 as book_id, 2 as genre_id union all
            select 2 as book_id, 3 as genre_id union all
            select 2 as book_id, 4 as genre_id union all
            select 3 as book_id, 5 as genre_id union all
            select 3 as book_id, 6 as genre_id
        ) src ON src.book_id = dst.book_id and src.genre_id = dst.genre_id
when not matched then
    insert (book_id, genre_id)
    values (src.book_id, src.genre_id);

merge into users as dst
    using
        (
            select 'admin' as username, '$2a$10$NUwhgtEr7LV.2NsERGa1U.bVk6fCZyed2y7pip0aswyivy10uqOK2' as password, 'ADMIN' as role union all
            select 'user' as username, '$2a$10$H0eVrjuyLIE/GJcJ5VaaNu.OyjZaueCMxcYeEZK2TFO4dF5T2UVhG' as password, 'USER' as role
        ) src ON src.username = dst.username
    when not matched then
        insert (username, password, role)
            values (src.username, src.password, src.role)
    when matched then
        update
            set username = src.username,
                password = src.password,
                role = src.role;
