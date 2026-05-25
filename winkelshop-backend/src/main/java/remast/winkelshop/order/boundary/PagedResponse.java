package remast.winkelshop.order.boundary;

import lombok.Builder;

import java.util.List;
import java.util.function.Function;

@Builder
public record PagedResponse<T>(
        List<T> content,
        PageableDetails pageable,
        boolean last,
        int totalPages,
        long totalElements,
        int size,
        int number,
        SortDetails sort,
        int numberOfElements
) {

    public <R> PagedResponse<R> map(Function<T, R> mapper) {
        return PagedResponse.<R>builder()
                .content(content.stream().map(mapper).toList())
                .pageable(pageable)
                .last(last)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .size(size)
                .number(number)
                .sort(sort)
                .numberOfElements(numberOfElements)
                .build();
    }

    public static <T> PagedResponse<T> of(List<T> all, int page, int size) {
        var safePage = Math.max(page, 0);
        var safeSize = Math.max(size, 1);
        var totalElements = all.size();
        var from = Math.min(safePage * safeSize, totalElements);
        var to = Math.min(from + safeSize, totalElements);
        var content = all.subList(from, to);
        var totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / safeSize);

        return PagedResponse.<T>builder()
                .content(content)
                .pageable(new PageableDetails(safePage, safeSize, new SortDetails(false, true, true), (long) safePage * safeSize, true, false))
                .last(safePage >= totalPages - 1)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .size(safeSize)
                .number(safePage)
                .sort(new SortDetails(false, true, true))
                .numberOfElements(content.size())
                .build();
    }

    public static <T> PagedResponse<T> fromContentAndTotal(List<T> content, int page, int size, long totalElements) {
        var safePage = Math.max(page, 0);
        var safeSize = Math.max(size, 1);
        var totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / safeSize);

        return PagedResponse.<T>builder()
                .content(content)
                .pageable(new PageableDetails(safePage, safeSize, new SortDetails(false, true, true), (long) safePage * safeSize, true, false))
                .last(safePage >= totalPages - 1)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .size(safeSize)
                .number(safePage)
                .sort(new SortDetails(false, true, true))
                .numberOfElements(content.size())
                .build();
    }

    public record PageableDetails(
            int pageNumber,
            int pageSize,
            SortDetails sort,
            long offset,
            boolean paged,
            boolean unpaged
    ) {
    }

    public record SortDetails(boolean sorted, boolean unsorted, boolean empty) {
    }
}
