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
            select 'BookTitle_1' as title, 1 as author_id, 1 as genre_id union all
            select 'BookTitle_2' as title, 2 as author_id, 2 as genre_id union all
            select 'BookTitle_3' as title, 3 as author_id, 3 as genre_id
        ) src ON src.title = dst.title
    when not matched then
        insert (title, author_id, genre_id)
            values (src.title, src.author_id, src.genre_id)
    when matched then
        update
            set title = src.title, author_id = src.author_id, genre_id = src.genre_id;