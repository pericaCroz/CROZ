package hr.croz.zadaci;

/*
 * XAdES4j - A Java library for generation and verification of XAdES signatures.
 * Copyright (C) 2010 Luis Goncalves.
 *
 * XAdES4j is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or any later version.
 *
 * XAdES4j is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with XAdES4j. If not, see <http://www.gnu.org/licenses/>.
 */

import java.security.cert.X509Certificate;

import xades4j.providers.impl.KeyStoreKeyingDataProvider.KeyEntryPasswordProvider;
import xades4j.providers.impl.KeyStoreKeyingDataProvider.KeyStorePasswordProvider;

/**
 *
 * @author Luis
 */
public class DirectPasswordProvider implements KeyStorePasswordProvider,
        KeyEntryPasswordProvider
{
    private char[] password;

    public DirectPasswordProvider(String password)
    {
        this.password = password.toCharArray();
    }

    @Override
    public char[] getPassword()
    {
        return password;
    }

    @Override
    public char[] getPassword(String entryAlias, X509Certificate entryCert)
    {
        return password;
    }
}
