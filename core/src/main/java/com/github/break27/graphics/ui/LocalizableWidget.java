/**************************************************************************
 * Copyright (c) 2021 Breakerbear
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *************************************************************************/

package com.github.break27.graphics.ui;

import com.badlogic.gdx.utils.I18NBundle;
import com.github.break27.system.Locales;

public interface LocalizableWidget extends AlternativeWidget {

    default I18NBundle getLocale() {
        return Locales.getBundle("default");
    }

    default String translate(String code) {
        return Locales.translate("default", this.getClass().getSimpleName(), code);
    }

    default String translate(String code, Object... args) {
        return Locales.translate("default", this.getClass().getSimpleName(), code, args);
    }

    default String translate(String component, String code) {
        return Locales.translate("default", this.getClass().getSimpleName() +
                "." + component, code);
    }

    default String translate(String component, String code, Object... args) {
        return Locales.translate("default", this.getClass().getSimpleName() +
                "." + component, code, args);
    }

    void localeApply();
}
