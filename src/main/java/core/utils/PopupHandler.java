package core.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Utility class for handling popups consistently across all test classes.
 * Uses JavaScript-based immediate popup handling for faster execution.
 */
public class PopupHandler {
    
    /**
     * Handle all common popups immediately using JavaScript.
     * This method closes "No Thanks" popups and modal popups without waiting.
     * 
     * @param driver The WebDriver instance
     */
    public static void handleAllPopupsImmediately(WebDriver driver) {
        try {
            // ULTRA-AGGRESSIVE popup handling - multiple strategies to ensure ALL popups are closed immediately
            ((JavascriptExecutor) driver).executeScript(
                "// Strategy 1: Click No Thanks button in beamerPushModal immediately " +
                "var noThanksBtn = document.getElementById('pushActionRefuse'); " +
                "if (noThanksBtn && noThanksBtn.style.display !== 'none') { " +
                "    noThanksBtn.click(); " +
                "    console.log('Clicked No Thanks button in beamerPushModal'); " +
                "} " +
                "// Strategy 2: Remove the entire beamerPushModal " +
                "var beamerModal = document.getElementById('beamerPushModal'); " +
                "if (beamerModal) { " +
                "    beamerModal.remove(); " +
                "    console.log('Removed beamerPushModal'); " +
                "} " +
                "// Strategy 3: Remove pushActions div - CRITICAL FOR CLICK INTERCEPTION " +
                "var pushActions = document.querySelector('.pushActions'); " +
                "if (pushActions) { " +
                "    pushActions.remove(); " +
                "    console.log('Removed pushActions div'); " +
                "} " +
                "// Strategy 4: Remove any elements with pushModal class " +
                "var pushModals = document.querySelectorAll('.pushModal, [class*=\"pushModal\"]'); " +
                "for (var i = 0; i < pushModals.length; i++) { " +
                "    pushModals[i].remove(); " +
                "    console.log('Removed pushModal element'); " +
                "} " +
                "// Strategy 5: Hide and remove ALL modals, popups, overlays " +
                "var allPopups = document.querySelectorAll('.modal, .popup, .overlay, .backdrop, .beamer_defaultBeamerSelector, [class*=\"modal\"], [class*=\"popup\"], [class*=\"overlay\"]'); " +
                "for (var i = 0; i < allPopups.length; i++) { " +
                "    allPopups[i].style.display = 'none'; " +
                "    allPopups[i].style.visibility = 'hidden'; " +
                "    allPopups[i].remove(); " +
                "    console.log('Removed popup/overlay element'); " +
                "} " +
                "// Strategy 6: Click ALL close buttons " +
                "var closeButtons = document.querySelectorAll('.close, .modal-close, .popup-close, [class*=\"close\"], button[aria-label*=\"Close\"], button[title*=\"Close\"]'); " +
                "for (var i = 0; i < closeButtons.length; i++) { " +
                "    if (closeButtons[i].style.display !== 'none') { " +
                "        closeButtons[i].click(); " +
                "        console.log('Clicked close button'); " +
                "    } " +
                "} " +
                "// Strategy 7: Remove any remaining notification elements " +
                "var notifications = document.querySelectorAll('[id*=\"beamer\"], [class*=\"beamer\"], [id*=\"notification\"], [class*=\"notification\"]'); " +
                "for (var i = 0; i < notifications.length; i++) { " +
                "    notifications[i].remove(); " +
                "    console.log('Removed notification element'); " +
                "} " +
                "// Strategy 8: Aggressively remove beamerSelector " +
                "var beamer = document.getElementById('beamerSelector'); " +
                "if (beamer) { " +
                "    beamer.style.display = 'none'; " +
                "    beamer.style.visibility = 'hidden'; " +
                "    beamer.style.opacity = '0'; " +
                "    beamer.style.pointerEvents = 'none'; " +
                "    beamer.remove(); " +
                "    console.log('Removed beamerSelector'); " +
                "} " +
                "// Strategy 9: Remove any elements with bottom-right positioning " +
                "var bottomRightElements = document.querySelectorAll('[class*=\"bottom-right\"], [style*=\"bottom-right\"]'); " +
                "for (var i = 0; i < bottomRightElements.length; i++) { " +
                "    bottomRightElements[i].remove(); " +
                "    console.log('Removed bottom-right element'); " +
                "} " +
                "// Strategy 10: NEW - Remove ALL elements that might intercept clicks " +
                "var clickInterceptors = document.querySelectorAll('[class*=\"push\"], [id*=\"push\"], [class*=\"notification\"], [class*=\"toast\"], [class*=\"alert\"]'); " +
                "for (var i = 0; i < clickInterceptors.length; i++) { " +
                "    clickInterceptors[i].remove(); " +
                "    console.log('Removed click interceptor element'); " +
                "} " +
                "// Strategy 11: NEW - Force hide any remaining floating elements " +
                "var floatingElements = document.querySelectorAll('[style*=\"position: fixed\"], [style*=\"position:absolute\"], [style*=\"z-index\"]'); " +
                "for (var i = 0; i < floatingElements.length; i++) { " +
                "    var elem = floatingElements[i]; " +
                "    if (elem.style.zIndex > 1000 || elem.style.position === 'fixed' || elem.style.position === 'absolute') { " +
                "        elem.style.display = 'none'; " +
                "        elem.style.visibility = 'hidden'; " +
                "        elem.style.opacity = '0'; " +
                "        elem.style.pointerEvents = 'none'; " +
                "        elem.remove(); " +
                "        console.log('Removed floating element'); " +
                "    } " +
                "} " +
                "// Strategy 12: NEW - Clear any remaining event listeners that might recreate popups " +
                "if (window.beamer) { " +
                "    window.beamer = null; " +
                "    console.log('Cleared beamer object'); " +
                "} " +
                "// Strategy 13: NEW - Remove any iframes that might contain popups " +
                "var iframes = document.querySelectorAll('iframe[src*=\"beamer\"], iframe[src*=\"notification\"]'); " +
                "for (var i = 0; i < iframes.length; i++) { " +
                "    iframes[i].remove(); " +
                "    console.log('Removed popup iframe'); " +
                "}"
            );
            
            System.out.println("Handled ALL popups ultra-aggressively with JavaScript");
        } catch (Exception e) {
            System.out.println("Ultra-aggressive popup handling failed: " + e.getMessage());
        }
    }
    
    /**
     * Continuously monitor and close "No Thanks" popups for a specified duration.
     * This is especially useful after clicking Create Template or Create New buttons.
     * 
     * @param driver The WebDriver instance
     * @param durationSeconds How long to monitor for popups (default 5 seconds)
     */
    public static void continuouslyCloseNoThanksPopup(WebDriver driver, int durationSeconds) {
        try {
            long endTime = System.currentTimeMillis() + (durationSeconds * 1000);
            while (System.currentTimeMillis() < endTime) {
                ((JavascriptExecutor) driver).executeScript(
                    "// Check for and immediately close beamerPushModal " +
                    "var noThanksBtn = document.getElementById('pushActionRefuse'); " +
                    "if (noThanksBtn && noThanksBtn.style.display !== 'none') { " +
                    "    noThanksBtn.click(); " +
                    "    console.log('Clicked No Thanks button in continuous monitoring'); " +
                    "} " +
                    "var beamerModal = document.getElementById('beamerPushModal'); " +
                    "if (beamerModal) { " +
                    "    beamerModal.remove(); " +
                    "    console.log('Removed beamerPushModal in continuous monitoring'); " +
                    "} " +
                    "var pushActions = document.querySelector('.pushActions'); " +
                    "if (pushActions) { " +
                    "    pushActions.remove(); " +
                    "    console.log('Removed pushActions in continuous monitoring'); " +
                    "} " +
                    "// Also handle beamerSelector " +
                    "var beamer = document.getElementById('beamerSelector'); " +
                    "if (beamer) { " +
                    "    beamer.remove(); " +
                    "    console.log('Removed beamerSelector in continuous monitoring'); " +
                    "}"
                );
                Thread.sleep(100); // Check every 100ms
            }
            System.out.println("Finished continuous popup monitoring for " + durationSeconds + " seconds");
        } catch (Exception e) {
            System.out.println("Continuous popup monitoring failed: " + e.getMessage());
        }
    }
    
    /**
     * AGGRESSIVE popup removal before critical operations.
     * This method should be called before any click operation that might be intercepted.
     * 
     * @param driver The WebDriver instance
     */
    public static void aggressivelyRemoveAllPopupsBeforeClick(WebDriver driver) {
        try {
            // First, handle all popups normally
            handleAllPopupsImmediately(driver);
            
            // Then, wait a moment and handle again to catch any that reappeared
            Thread.sleep(200);
            
            // Finally, do one more aggressive cleanup
            ((JavascriptExecutor) driver).executeScript(
                "// FINAL AGGRESSIVE CLEANUP - Remove ANYTHING that might interfere " +
                "var allElements = document.querySelectorAll('*'); " +
                "for (var i = 0; i < allElements.length; i++) { " +
                "    var elem = allElements[i]; " +
                "    var className = elem.className || ''; " +
                "    var id = elem.id || ''; " +
                "    var style = elem.style || {}; " +
                "    " +
                "    // Check if this element looks like a popup or notification " +
                "    if (className.includes('push') || id.includes('push') || " +
                "        className.includes('notification') || id.includes('notification') || " +
                "        className.includes('toast') || id.includes('toast') || " +
                "        className.includes('alert') || id.includes('alert') || " +
                "        className.includes('modal') || id.includes('modal') || " +
                "        className.includes('popup') || id.includes('popup') || " +
                "        className.includes('beamer') || id.includes('beamer') || " +
                "        style.position === 'fixed' || style.position === 'absolute' || " +
                "        parseInt(style.zIndex) > 1000) { " +
                "        " +
                "        // Aggressively remove this element " +
                "        elem.style.display = 'none'; " +
                "        elem.style.visibility = 'hidden'; " +
                "        elem.style.opacity = '0'; " +
                "        elem.style.pointerEvents = 'none'; " +
                "        elem.remove(); " +
                "        console.log('AGGRESSIVELY removed interfering element: ' + className + ' ' + id); " +
                "    } " +
                "} " +
                "// Also clear any global objects that might recreate popups " +
                "if (window.beamer) window.beamer = null; " +
                "if (window.pushNotification) window.pushNotification = null; " +
                "console.log('AGGRESSIVE popup cleanup completed');"
            );
            
            System.out.println("AGGRESSIVE popup removal completed before click operation");
        } catch (Exception e) {
            System.out.println("AGGRESSIVE popup removal failed: " + e.getMessage());
        }
    }
    
    /**
     * SPECIFICALLY target and remove the pushActions div that's causing click interception.
     * This method focuses on the exact element that's causing issues.
     * 
     * @param driver The WebDriver instance
     */
    public static void specificallyRemovePushActionsDiv(WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "// SPECIFICALLY target pushActions div that's causing click interception " +
                "var pushActions = document.querySelector('.pushActions'); " +
                "if (pushActions) { " +
                "    pushActions.remove(); " +
                "    console.log('SPECIFICALLY removed pushActions div'); " +
                "} " +
                "// Also remove any elements with similar names " +
                "var similarElements = document.querySelectorAll('[class*=\"push\"], [id*=\"push\"]'); " +
                "for (var i = 0; i < similarElements.length; i++) { " +
                "    similarElements[i].remove(); " +
                "    console.log('Removed similar push element'); " +
                "} " +
                "// Remove any floating elements that might intercept clicks " +
                "var floatingElements = document.querySelectorAll('[style*=\"position: fixed\"], [style*=\"position:absolute\"]'); " +
                "for (var i = 0; i < floatingElements.length; i++) { " +
                "    var elem = floatingElements[i]; " +
                "    if (elem.style.zIndex > 1000 || elem.style.position === 'fixed' || elem.style.position === 'absolute') { " +
                "        elem.remove(); " +
                "        console.log('Removed floating element that might intercept clicks'); " +
                "    } " +
                "}"
            );
            System.out.println("SPECIFICALLY removed pushActions div and similar elements");
        } catch (Exception e) {
            System.out.println("Specific pushActions removal failed: " + e.getMessage());
        }
    }
    
    /**
     * Handle only "No Thanks" popup immediately using JavaScript.
     * 
     * @param driver The WebDriver instance
     */
    public static void closeNoThanksPopupImmediately(WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "var noThanksBtn = document.getElementById('pushActionRefuse'); " +
                "if (noThanksBtn && noThanksBtn.style.display !== 'none') { " +
                "    noThanksBtn.click(); " +
                "    noThanksBtn.remove(); " +
                "    console.log('Closed No Thanks popup with JS'); " +
                "}"
            );
        } catch (Exception e) {
            System.out.println("JavaScript No Thanks popup handling failed: " + e.getMessage());
        }
    }
    
    /**
     * Handle only modal popups immediately using JavaScript.
     * 
     * @param driver The WebDriver instance
     */
    public static void closeModalPopupsImmediately(WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "var closeBtns = document.querySelectorAll('div.modal-content div.modal-header button, .popup-close, .RCB-modal-close'); " +
                "for (var i = 0; i < closeBtns.length; i++) { " +
                "    if (closeBtns[i].style.display !== 'none') { " +
                "        closeBtns[i].click(); " +
                "        console.log('Closed modal popup with JS'); " +
                "        break; " +
                "    } " +
                "}"
            );
        } catch (Exception e) {
            System.out.println("JavaScript modal popup handling failed: " + e.getMessage());
        }
    }
    
    /**
     * Handle beamerSelector notification button immediately.
     * 
     * @param driver The WebDriver instance
     */
    public static void handleBeamerSelectorImmediately(WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "var beamer = document.getElementById('beamerSelector'); " +
                "if (beamer) { " +
                "    beamer.style.display = 'none'; " +
                "    beamer.style.visibility = 'hidden'; " +
                "    beamer.style.opacity = '0'; " +
                "    beamer.style.pointerEvents = 'none'; " +
                "    beamer.remove(); " +
                "    console.log('Removed beamerSelector immediately'); " +
                "}"
            );
        } catch (Exception e) {
            System.out.println("BeamerSelector handling failed: " + e.getMessage());
        }
    }
    
    /**
     * Handle beamerPushModal notification popup immediately.
     * 
     * @param driver The WebDriver instance
     */
    public static void handleBeamerPushModalImmediately(WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "// Click No Thanks button if present " +
                "var noThanksBtn = document.getElementById('pushActionRefuse'); " +
                "if (noThanksBtn && noThanksBtn.style.display !== 'none') { " +
                "    noThanksBtn.click(); " +
                "    console.log('Clicked No Thanks button in beamerPushModal'); " +
                "} " +
                "// Remove the entire beamerPushModal " +
                "var beamerModal = document.getElementById('beamerPushModal'); " +
                "if (beamerModal) { " +
                "    beamerModal.remove(); " +
                "    console.log('Removed beamerPushModal immediately'); " +
                "} " +
                "// Remove pushActions div " +
                "var pushActions = document.querySelector('.pushActions'); " +
                "if (pushActions) { " +
                "    pushActions.remove(); " +
                "    console.log('Removed pushActions div immediately'); " +
                "}"
            );
        } catch (Exception e) {
            System.out.println("BeamerPushModal handling failed: " + e.getMessage());
        }
    }
} 