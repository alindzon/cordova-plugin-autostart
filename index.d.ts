declare module 'cordova-plugin-autostart' {
    export function enable(): Promise<void>;
    export function disable(): Promise<void>;
    export function getMode(): Promise<{value: string}>;
    export function setMode(mode: string): Promise<void>;
}
