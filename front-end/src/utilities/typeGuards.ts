export function isNotNull<T>(value: T | null): value is T {
    return value !== null;
}

export function isNotUndefined<T>(value: T | undefined): value is T {
    return value !== undefined;
}

export async function fetchWithHandling<T>(url: string, options: RequestInit): Promise<T> {
    const response = await fetch(url, options);
    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'An error occurred');
    }
    return response.json() as Promise<T>;
}

